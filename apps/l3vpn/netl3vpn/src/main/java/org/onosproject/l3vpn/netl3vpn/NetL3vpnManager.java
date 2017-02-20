/*
 * Copyright 2017-present Open Networking Laboratory
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
package org.onosproject.l3vpn.netl3vpn;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.l3vpn.svc.rev20160730.IetfL3VpnSvc;
import org.onosproject.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.l3vpn.svc.rev20160730.ietfl3vpnsvc.L3VpnSvc;
import org.onosproject.yang.gen.v1.urn.ietf.params.xml.ns.yang.l3vpn.svc.ext.rev20160730.L3VpnSvcExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The IETF net l3vpn manager implementation.
 */
@Component(immediate = true)
@Service
public class NetL3vpnManager implements IetfL3VpnSvc, L3VpnSvcExt {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Activate
    protected void activate() {
        log.info("Started");
    }

    @Deactivate
    protected void deactivate() {
        log.info("Stopped");
    }

    @Override
    public L3VpnSvc l3VpnSvc() {
        // TODO implementation
        return null;
    }

    @Override
    public void l3VpnSvc(L3VpnSvc l3VpnSvc) {
        // TODO implementation
    }
}
