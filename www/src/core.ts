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

    private serverIPs: { [id: string]: string } = {};
    private clicked: boolean = false;
    private connection: string = "";
    private monitorRoot: MonitorRoot;

    public init(monitorRoot: MonitorRoot): void {
        this.monitorRoot = monitorRoot;
        const self: Core = this;

        self.monitorRoot.refresh();
        setInterval(function () {
            self.monitorRoot.refresh();
        }, 10_000);
    }

    private saveSettings() {
        this.clicked = true;
        const data = (<HTMLTextAreaElement>document.querySelector(".settings > textarea")).value;

        const self: Core = this;
        fetch("http://" + this.connection + "/config?persist=true", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: data
        }).then((response) => {
            self.clicked = false;
        }).catch((e) => {
            console.log(e);
            self.clicked = false;
        });
    }
}

declare global {
    interface Window {
        core: Core;
    }
}

window.core = new Core();