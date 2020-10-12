import {
    CSSResult,
    customElement,
    LitElement,
    TemplateResult,
    unsafeCSS
} from 'lit-element';
import entryStyle from "./server-entry.less";
import {ServerInfo} from "../rest/types";
import {ProgressBar} from "../progress-bar/progress-bar";
import {serverEntryTemplate} from "./server-entry-template";

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
        return serverEntryTemplate(this);
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

    private clicked(): void {
        this.dispatchEvent(new ServerEntrySelectedEvent(this.server));
    }
}