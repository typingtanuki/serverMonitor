import {html, TemplateResult} from "lit-element";
import {ServerEntry} from "./server-entry";
import {icon, Icon, iconServerNg, iconServerOK} from "../icon-svg/icons";
import {Detail, Monitor, MonitorType, ReportConstants} from "../rest/types";

export function serverEntryTemplate(serverEntry: ServerEntry): TemplateResult {
    serverEntry.classList.add("OK");
    serverEntry.classList.remove("NG");
    let ok: boolean = true;
    for (const monitor of serverEntry.server.monitors) {
        if (!monitor.state) {
            serverEntry.classList.remove("OK");
            serverEntry.classList.add("NG");
            ok = false;
        }
    }
    const statusIcon: Icon = ok ? icon(iconServerOK) : icon(iconServerNg);
    return html`<icon-svg .icon="${statusIcon}"></icon-svg>
        <div class="title">${serverEntry.server.name}</div>
        ${serverEntry.server.monitors.map(monitor => formatMonitor(monitor))}`;
}

function formatMonitor(monitor: Monitor): TemplateResult {
    let advanced: TemplateResult = html``;

    if (monitor.advanced && monitor.advanced.length > 0) {
        switch (monitor.name) {
            case MonitorType.disk:
            case MonitorType.cpu:
            case MonitorType.memory:
                advanced =
                    html`${monitor.advanced.map(detail => formatDetails(
                        monitor.name,
                        detail))}`;
                break
            case MonitorType.handshake:
                advanced =
                    html`<div class="shakebox">${monitor.advanced.map(
                        detail => formatHandshake(detail))}</div>`;
                break;
        }
    }

    return html`<div class="monitor ${monitor.state ?
        'OK' :
        'NG'}">${monitor.name}${advanced}</div>`;
}

function formatDetails(type: MonitorType, details: Detail): TemplateResult {
    const sub = details["details"];
    const current: number = parseInt(sub[ReportConstants.USAGE_CURRENT], 10);
    const warn: number = parseInt(sub[ReportConstants.USAGE_MAX], 10);
    let label: string = "";
    if (sub.hasOwnProperty(ReportConstants.DISK)) {
        label = sub[ReportConstants.DISK];
    }
    if (details) {
        return html`<progress-bar type="${type}"
                                  min="0"
                                  max="100"
                                  current='${current}'
                                  warn='${warn}'
                                  label="${label}"></progress-bar>`;
    }
}

function formatHandshake(details: Detail): TemplateResult {
    return html`<div class="shake ${details["ok"] ?
        "OK" :
        "NG"}" title="${details["description"]}"></div>`;
}