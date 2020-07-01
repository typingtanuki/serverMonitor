import {MonitorRoot} from "./monitor-root/monitor-root";
import {html, TemplateResult} from "lit-element";

interface Monitor {
    name: string;
    value: boolean
}

interface Server {
    children: Monitor[];
    name: string;
}

export class Core {
    public dependencies: any[] = [MonitorRoot];

    private monitorRoot: MonitorRoot;

    public init(monitorRoot: MonitorRoot): void {
        this.monitorRoot = monitorRoot;
        const self: Core = this;

        self.monitorRoot.refresh();
        setInterval(function () {
            self.monitorRoot.refresh();
        }, 10_000);
    }

    public async showModal(header: string, content: TemplateResult): Promise<void> {
        return this.monitorRoot.showModal(header, content);
    }

    public async showError(error: string | Error): Promise<void> {
        return this.showModal("Error", this.formatMessage(error));
    }

    public closeDetails(): void {
        this.monitorRoot.closeDetails();
        this.monitorRoot.refresh();
    }

    private formatMessage(error: any): TemplateResult {
        if (error === null || error === undefined) {
            return html`<div>No error details</div>`;
        }
        if (error instanceof Error) {
            const err: Error = <Error>error;
            // Looks like an error
            return html`<div class="info">${err.message}</div><div class="info">${JSON.stringify(err.stack, null, 4)}</div>`;
        }
        return html`<div class="info">${String(error)}</div>`
    }
}

declare global {
    interface Window {
        core: Core;
    }
}

window.core = new Core();