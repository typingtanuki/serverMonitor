import {MonitorRoot} from "./monitor-root/monitor-root";

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
}

declare global {
    interface Window {
        core: Core;
    }
}

window.core = new Core();