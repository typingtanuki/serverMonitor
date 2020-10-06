import {
    CSSResult,
    customElement,
    html,
    LitElement,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import entryStyle from "./server-entry.less";
import {Detail, Monitor, MonitorType, ReportConstants, ServerInfo} from "../rest/types";
import {ProgressBar} from "../progress-bar/progress-bar";
import {icon, Icon, iconBim, iconCollapse} from "../icon-svg/icons";

export interface ServerEntrySelectedEventInfo {
    server: ServerInfo;
}

export class ServerEntrySelectedEvent extends CustomEvent<ServerEntrySelectedEventInfo> {
    public static get event(): string {
        return "server-entry-selected";
    }

    constructor(serverInfo: ServerInfo) {
        super(ServerEntrySelectedEvent.event,
            {bubbles: true, composed: true, detail: {server: serverInfo}});
    }
}

@customElement('server-entry')
export class ServerEntry extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(entryStyle);
    }

    static get properties() {
        return {
            server: {type: Object}
        };
    }

    public server: ServerInfo;

    constructor() {
        super();
    }

    public render(): TemplateResult {
        this.classList.add("OK");
        this.classList.remove("NG");
        let ok: boolean = true;
        for (const monitor of this.server.monitors) {
            if (!monitor.state) {
                this.classList.remove("OK");
                this.classList.add("NG");
                ok = false;
            }
        }
        const statusIcon: Icon = ok ? icon(iconCollapse) : icon(iconBim);
        return html`<icon-svg .icon="${statusIcon}"></icon-svg>
        <div class="title">${this.server.name}</div>
        ${this.server.monitors.map(monitor => ServerEntry.formatMonitor(monitor))}`;
    }

    public async refresh(): Promise<void> {
        await this.requestUpdate();
        const bars: NodeListOf<ProgressBar> = this.shadowRoot.querySelectorAll(
            "progress-bar");
        for (let i = 0; i < bars.length; i++) {
            await bars[i].requestUpdate();
        }
    }

    public firstUpdated(): void {
        const self: ServerEntry = this;
        this.addEventListener('click', function () {
            self.clicked();
        });
    }

    private static formatMonitor(monitor: Monitor): TemplateResult {
        let advanced: TemplateResult = html``;

        if (monitor.advanced && monitor.advanced.length > 0) {
            switch (monitor.name) {
                case MonitorType.disk:
                case MonitorType.cpu:
                case MonitorType.memory:
                    advanced =
                        html`${monitor.advanced.map(detail => ServerEntry.formatDetails(
                            monitor.name,
                            detail))}`;
                    break
                case MonitorType.handshake:
                    advanced =
                        html`<div class="shakebox">${monitor.advanced.map(detail => ServerEntry.formatHandshake(
                            detail))}</div>`;
                    break;
            }
        }

        return html`<div class="monitor ${monitor.state ?
            'OK' :
            'NG'}">${monitor.name}${advanced}</div>`;
    }

    private static formatDetails(type: MonitorType, details: Detail): TemplateResult {
        const sub = details["details"];
        const current: number = parseInt(sub[ReportConstants.USAGE_CURRENT], 10);
        const warn: number = parseInt(sub[ReportConstants.USAGE_MAX], 10);
        let label: string = "";
        if (sub.hasOwnProperty(ReportConstants.DISK)) {
            label = sub[ReportConstants.DISK];
        }
        if (details) {
            return html`<progress-bar type="${type}" min="0" max="100" current='${current}' warn='${warn}' label="${label}"></progress-bar>`;
        }
    }

    private static formatHandshake(details: Detail): TemplateResult {
        return html`<div class="shake ${details["ok"] ?
            "OK" :
            "NG"}" title="${details["description"]}"></div>`;
    }

    private clicked(): void {
        this.dispatchEvent(new ServerEntrySelectedEvent(this.server));
    }
}