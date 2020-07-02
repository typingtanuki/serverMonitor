import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from 'lit-element';
import entryStyle from "./server-entry.less";
import {Monitor, ServerInfo} from "../rest/types";

export interface ServerEntrySelectedEventInfo {
    server: ServerInfo;
}

export class ServerEntrySelectedEvent extends CustomEvent<ServerEntrySelectedEventInfo> {
    public static get event(): string {
        return "server-entry-selected";
    }

    constructor(serverInfo: ServerInfo) {
        super(ServerEntrySelectedEvent.event, {bubbles: true, composed: true, detail: {server: serverInfo}});
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
        for (const monitor of this.server.monitors) {
            if (!monitor.state) {
                this.classList.remove("OK");
                this.classList.add("NG");
            }
        }
        return html`<div class="title">${this.server.name}</div>
${this.server.monitors.map(monitor => ServerEntry.formatMonitor(monitor))}`;
    }

    public firstUpdated(): void {
        const self: ServerEntry = this;
        this.addEventListener('click', function () {
            self.clicked();
        });
    }

    private static formatMonitor(monitor: Monitor): TemplateResult {
        return html`<div class="${monitor.state ? 'OK' : 'NG'}">${monitor.name}</div>`;
    }

    private clicked(): void {
        this.dispatchEvent(new ServerEntrySelectedEvent(this.server));
    }
}