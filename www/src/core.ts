import {div, li, text, ul} from "./layout";
import {formatReports} from "./report";
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

    private enableSettingsButton(): void {
        const elem = document.getElementById("settings");
        if (elem == null) {
            return;
        }
        if (elem.classList.contains("active")) {
            return;
        }
        elem.classList.add("active");

        const self: Core = this;
        elem.addEventListener("click", function () {
            self.showSettings();
        });
    }

    private enableSaveButton() {
        const elem = document.getElementById("save");
        if (elem == null) {
            return;
        }
        if (elem.classList.contains("active")) {
            return;
        }
        elem.classList.add("active");

        const self: Core = this;
        elem.addEventListener("click", function () {
            self.saveSettings();
        });
    }

    private showSettings() {
        document.querySelector(".details").classList.remove("half");
        document.querySelector(".settings").classList.add("half");

        this.clicked = true;
        this.fetchSettings();
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



    private fetchSettings() {
        const self: Core = this;
        requestAnimationFrame(function () {
            self.enableSaveButton();
        });
        fetch("http://" + this.connection + "/config")
            .then((response) => {
                return response.json();
            })
            .then((response) => {
                (<HTMLTextAreaElement>document.querySelector(".settings > textarea")).value = JSON.stringify(response, null, 4);
                self.clicked = false;

            })
            .catch((e) => {
                document.getElementById("failure").textContent = JSON.stringify(e, null, 4);
                self.clicked = false;
            });
    }

    private createServer(server: Server): void {
        var name = server.name;
        var children = server.children;
        var entry = li();
        entry.classList.add("server");
        const self: Core = this;
        entry.addEventListener("click", function () {
            if (self.clicked) {
                return;
            }
            document.querySelector(".details").classList.remove("half");
            document.querySelector(".settings").classList.remove("half");

            self.clicked = true;

            const serverName = server.name;
            if (self.serverIPs.hasOwnProperty(serverName)) {
                self.connection = self.serverIPs[serverName];
            } else {
                self.connection = self.currentServer();
            }
            document.getElementById("server").textContent = serverName;
            document.getElementById("failure").textContent = "Updating, please wait...";
            document.getElementById("success").textContent = "Updating, please wait...";
            document.querySelector(".chart").classList.add("half");
            document.querySelector(".details").classList.add("half");
            requestAnimationFrame(function () {
                self.enableSettingsButton();
            });
            self.fetchDetails();
        });

        entry.appendChild(div(text(name)));
        var details = ul();
        entry.appendChild(details);
        for (var i = 0; i < children.length; i++) {
            var child = children[i];
            var elem = li(text(child.name));
            if (child.value) {
                elem.classList.add("OK");
            } else {
                elem.classList.add("NG")
            }
            details.appendChild(elem);
        }
    }
}

declare global {
    interface Window {
        core: Core;
    }
}

window.core = new Core();