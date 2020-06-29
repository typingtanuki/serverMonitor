import {ServerList} from "../server-list/server-list";
import {Report} from "../report";
import {DetailView} from "../detail-view/detail-view";

export interface ServerState {
    [key: string]: boolean;
}

export interface ServerStates {
    clusterStatus: { [id: string]: ServerState };
    connections: { [id: string]: string };
}

export interface SuccessFailureDetail {
    success: Report[];
    failure: Report[];
}

export interface DetailStatuses {
    status: SuccessFailureDetail;
}

export interface Monitor {
    name: string;
    state: boolean;
}

export interface ServerInfo {
    name: string;
    monitors: Monitor[];
}

export class RestClient {
    private readonly server: string;

    constructor(server: string) {
        this.server = server;
    }

    public async getServerState(serverList: ServerList): Promise<void> {
        const response: Response = await fetch(this.server + "/status/cluster");
        const serverState: ServerStates = await response.json();

        const cluster = serverState.clusterStatus;
        serverList.serverIPs = serverState.connections;

        const serverNames = Object.keys(cluster);
        serverNames.sort();

        const servers: ServerInfo[] = [];

        for (const serverName of serverNames) {
            const status: ServerState = cluster[serverName];

            const monitors: Monitor[] = [];
            const monitorNames: string[] = Object.keys(status);
            monitorNames.sort();
            let allOk = true;
            for (const monitorName of monitorNames) {
                const state = status[monitorName];
                if (!state) {
                    allOk = false;
                }
                monitors.push({
                    name: monitorName,
                    state: state
                });
            }

            servers.push({
                name: serverName,
                monitors: monitors
            });
        }

        serverList.servers = servers;
        await serverList.requestUpdate();
    }

    public async fetchDetails(detailView: DetailView): Promise<void> {
        const response: Response = await fetch("http://" + this.server + "/status")
        const status: DetailStatuses = await response.json();
        detailView.success = status.status.success;
        detailView.failure = status.status.failure;
        await detailView.redraw();
    }
}