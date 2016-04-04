/*
 * Copyright 2015-present Open Networking Laboratory
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onosproject.provider.pcep.tunnel.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.onosproject.net.DefaultAnnotations.EMPTY;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.onlab.packet.IpAddress;
import org.onosproject.cfg.ComponentConfigAdapter;
import org.onosproject.core.DefaultGroupId;
import org.onosproject.incubator.net.tunnel.DefaultTunnel;
import org.onosproject.incubator.net.tunnel.IpTunnelEndPoint;
import org.onosproject.incubator.net.tunnel.Tunnel;
import org.onosproject.incubator.net.tunnel.TunnelId;
import org.onosproject.incubator.net.tunnel.TunnelName;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DefaultLink;
import org.onosproject.net.DefaultPath;
import org.onosproject.net.IpElementId;
import org.onosproject.net.Link;
import org.onosproject.net.Path;
import org.onosproject.net.PortNumber;
import org.onosproject.net.provider.ProviderId;
import org.onosproject.pcepio.types.StatefulIPv4LspIdentidiersTlv;

/**
 * Test for PCEP update tunnel.
 */
public class PcepUpdateTunnelProviderTest {

    public static final String PROVIDER_ID = "org.onosproject.provider.tunnel.pcep";
    private PcepTunnelProvider tunnelProvider = new PcepTunnelProvider();
    private final TunnelProviderRegistryAdapter registry = new TunnelProviderRegistryAdapter();
    private final PcepClientControllerAdapter controller = new PcepClientControllerAdapter();
    private final PcepControllerAdapter ctl = new PcepControllerAdapter();
    private final PcepTunnelApiMapper pcepTunnelAPIMapper = new PcepTunnelApiMapper();
    private final TunnelServiceAdapter  tunnelService = new TunnelServiceAdapter();

    @Before
    public void setUp() throws IOException {
        tunnelProvider.tunnelProviderRegistry = registry;
        tunnelProvider.pcepClientController = controller;
        tunnelProvider.controller = ctl;
        tunnelProvider.pcepTunnelApiMapper = pcepTunnelAPIMapper;
        tunnelProvider.cfgService = new ComponentConfigAdapter();
        tunnelProvider.tunnelService = tunnelService;
        tunnelProvider.activate();
    }

    /**
     * Send update message to PCC.
     */
    @Test
    public void testCasePcepUpdateTunnel() {
        Tunnel tunnel;
        Path path;
        ProviderId pid = new ProviderId("pcep", PROVIDER_ID);
        List<Link> links = new ArrayList<Link>();
        IpAddress srcIp = IpAddress.valueOf(0xD010101);
        IpElementId srcElementId = IpElementId.ipElement(srcIp);

        IpAddress dstIp = IpAddress.valueOf(0xD010102);
        IpElementId dstElementId = IpElementId.ipElement(dstIp);

        IpTunnelEndPoint ipTunnelEndPointSrc;
        ipTunnelEndPointSrc = IpTunnelEndPoint.ipTunnelPoint(srcIp);

        IpTunnelEndPoint ipTunnelEndPointDst;
        ipTunnelEndPointDst = IpTunnelEndPoint.ipTunnelPoint(dstIp);

        ConnectPoint src = new ConnectPoint(srcElementId, PortNumber.portNumber(10023));

        ConnectPoint dst = new ConnectPoint(dstElementId, PortNumber.portNumber(10023));

        Link link = DefaultLink.builder().providerId(pid).src(src).dst(dst)
                .type(Link.Type.DIRECT).build();
        links.add(link);

        path = new DefaultPath(pid, links, 20, EMPTY);

        tunnel = new DefaultTunnel(pid, ipTunnelEndPointSrc, ipTunnelEndPointDst, Tunnel.Type.MPLS,
                                   new DefaultGroupId(0), TunnelId.valueOf(1), TunnelName.tunnelName("T123"),
                                   path, EMPTY);

        // for updating tunnel tunnel should exist in db
        PcepTunnelData pcepTunnelData = new PcepTunnelData(tunnel, path, RequestType.UPDATE);
        pcepTunnelData.setPlspId(1);
        StatefulIPv4LspIdentidiersTlv tlv = new StatefulIPv4LspIdentidiersTlv(0, (short) 1, (short) 2, 3, 4);
        pcepTunnelData.setStatefulIpv4IndentifierTlv(tlv);
        tunnelProvider.pcepTunnelApiMapper.addToTunnelIdMap(pcepTunnelData);

        tunnelProvider.pcepTunnelApiMapper.handleCreateTunnelRequestQueue(1, pcepTunnelData);

        tunnelProvider.updateTunnel(tunnel, path);
        assertThat(tunnelProvider.pcepTunnelApiMapper, not(nullValue()));
    }

    /**
     * Doesn't send update message because PCC doesn't supports PCE stateful capability.
     */
    @Test
    public void testCasePcepUpdateTunnel2() {
        Tunnel tunnel;
        Path path;
        ProviderId pid = new ProviderId("pcep", PROVIDER_ID);
        List<Link> links = new ArrayList<Link>();
        IpAddress srcIp = IpAddress.valueOf(0xC010103);
        IpElementId srcElementId = IpElementId.ipElement(srcIp);

        IpAddress dstIp = IpAddress.valueOf(0xD010102);
        IpElementId dstElementId = IpElementId.ipElement(dstIp);

        IpTunnelEndPoint ipTunnelEndPointSrc;
        ipTunnelEndPointSrc = IpTunnelEndPoint.ipTunnelPoint(srcIp);

        IpTunnelEndPoint ipTunnelEndPointDst;
        ipTunnelEndPointDst = IpTunnelEndPoint.ipTunnelPoint(dstIp);

        ConnectPoint src = new ConnectPoint(srcElementId, PortNumber.portNumber(10023));

        ConnectPoint dst = new ConnectPoint(dstElementId, PortNumber.portNumber(10023));

        Link link = DefaultLink.builder().providerId(pid).src(src).dst(dst)
                .type(Link.Type.DIRECT).build();
        links.add(link);

        path = new DefaultPath(pid, links, 20, EMPTY);

        tunnel = new DefaultTunnel(pid, ipTunnelEndPointSrc, ipTunnelEndPointDst, Tunnel.Type.MPLS,
                                   new DefaultGroupId(0), TunnelId.valueOf(1), TunnelName.tunnelName("T123"),
                                   path, EMPTY);

        // for updating tunnel tunnel should exist in db
        PcepTunnelData pcepTunnelData = new PcepTunnelData(tunnel, path, RequestType.UPDATE);
        pcepTunnelData.setPlspId(1);
        StatefulIPv4LspIdentidiersTlv tlv = new StatefulIPv4LspIdentidiersTlv(0, (short) 1, (short) 2, 3, 4);
        pcepTunnelData.setStatefulIpv4IndentifierTlv(tlv);
        tunnelProvider.pcepTunnelApiMapper.addToTunnelIdMap(pcepTunnelData);

        tunnelProvider.pcepTunnelApiMapper.handleCreateTunnelRequestQueue(1, pcepTunnelData);

        tunnelProvider.updateTunnel(tunnel, path);
        assertThat(tunnelProvider.pcepTunnelApiMapper.checkFromTunnelRequestQueue(1), is(false));
    }

    @After
    public void tearDown() throws IOException {
        tunnelProvider.deactivate();
        tunnelProvider.controller = null;
        tunnelProvider.pcepClientController = null;
        tunnelProvider.tunnelProviderRegistry = null;
    }
}
