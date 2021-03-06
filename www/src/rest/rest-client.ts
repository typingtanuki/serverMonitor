import {ServerList} from "../server-list/server-list";
import {DetailView} from "../detail-view/detail-view";
import {
    Detail,
    DetailStatuses,
    Monitor,
    MonitorType,
    ServerInfo,
    ServerState,
    ServerStates,
    Settings,
    SuccessFailureDetail
} from "./types";

export class RestClient {
    private readonly server: string;

    private static fetchingState: boolean = false;
    private static fetchingDetails: boolean = false;
    private static fetchingSettings: boolean = false;
    private static savingSettings: boolean = false;
    private static uploadingBundle: boolean = false;

    public static isBusy(): boolean {
        return RestClient.fetchingState ||
            RestClient.fetchingDetails ||
            RestClient.fetchingSettings ||
            RestClient.savingSettings ||
            RestClient.uploadingBundle;
    }

    constructor(server: string) {
        while (server.startsWith("http://")) {
            server = server.split("http://")[1];
        }
        while (server.startsWith("https://")) {
            server = server.split("https://")[1];
        }
        this.server = "http://" + server;
    }

    public async getServerState(serverList: ServerList): Promise<void> {
        if (RestClient.fetchingState) {
            return;
        }
        RestClient.fetchingState = true;

        try {
            const response: Response = await fetch(this.server + "/status/cluster");
            const serverState: ServerStates = await response.json();

            const cluster: { [id: string]: ServerState } = serverState.clusterStatus;
            let advanced: { [id: string]: SuccessFailureDetail } = serverState.advanced;
            if (advanced === undefined || advanced === null) {
                advanced = {};
            }
            serverList.serverIPs = serverState.connections;

            const serverNames = Object.keys(cluster);
            serverNames.sort();

            const servers: ServerInfo[] = [];

            for (const serverName of serverNames) {
                const status: ServerState = cluster[serverName];

                let advancedServer: SuccessFailureDetail | null | undefined = advanced[serverName];

                const monitors: Monitor[] = [];
                const monitorNames: string[] = Object.keys(status);
                monitorNames.sort();
                let allOk = true;
                for (const monitorName of monitorNames) {
                    const state: boolean = status[monitorName];
                    if (!state) {
                        allOk = false;
                    }

                    const allDetails: Detail[] = [];
                    if (advancedServer !== null && advancedServer !== undefined) {
                        if (advancedServer.success) {
                            allDetails.push(...advancedServer.success);
                        }
                        if (advancedServer.failure) {
                            allDetails.push(...advancedServer.failure);
                        }
                    }

                    const details: Detail[] = [];

                    for (const detail of allDetails) {
                        if (detail.type == monitorName) {
                            details.push(detail);
                        }
                    }

                    monitors.push({
                        name: <MonitorType>monitorName,
                        state: state,
                        advanced: details
                    });
                }

                servers.push({
                    name: serverName,
                    monitors: monitors
                });
            }

            serverList.servers = servers;
            await serverList.requestUpdate();
            RestClient.fetchingState = false;
        } catch (e) {
            RestClient.fetchingState = false;
            throw e;
        }
    }

    public async fetchDetails(detailView: DetailView): Promise<void> {
        if (RestClient.fetchingDetails) {
            return;
        }
        RestClient.fetchingDetails = true;

        try {
            const response: Response = await fetch(this.server + "/status")
            const status: DetailStatuses = await response.json();
            detailView.success = status.status.success;
            detailView.failure = status.status.failure;
            await detailView.redraw();
            RestClient.fetchingDetails = false;
        } catch (e) {
            RestClient.fetchingDetails = false;
            throw e;
        }
    }

    public async fetchSettings(detailView: DetailView): Promise<void> {
        if (RestClient.fetchingSettings) {
            return;
        }
        RestClient.fetchingSettings = true;

        try {
            const response = await fetch(this.server + "/config");
            detailView.settings = await response.json();
            await detailView.redraw();
            RestClient.savingSettings = false;
        } catch (e) {
            RestClient.savingSettings = false;
            throw e;
        }
    }

    public async saveSettings(settings: Settings): Promise<void> {
        if (RestClient.savingSettings) {
            return;
        }
        RestClient.fetchingSettings = true;

        try {
            await fetch(this.server + "/config?persist=true", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(settings)
            });
            RestClient.savingSettings = false;
        } catch (e) {
            RestClient.savingSettings = false;
            throw e;
        }
    }

    public async uploadBundle(formData: FormData): Promise<void> {
        if (RestClient.uploadingBundle) {
            return;
        }
        RestClient.uploadingBundle = true;

        try {
            await fetch(this.server + "/config/updateMonitor", {
                method: 'POST',
                headers: {
                    'Accept': 'application/json'
                },
                body: formData
            });
            RestClient.uploadingBundle = false;
        } catch (e) {
            RestClient.uploadingBundle = false;
            throw e;
        }
    }
}