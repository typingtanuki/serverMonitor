export interface HistoryDetail extends Detail {
    values: string[];
    dates: string[];
    max: string;
    limit: string;

}

export interface Detail {
    type?: string;

    [key: string]: any;
}

export interface Report {
    title: string;
    details: { [id: string]: Detail };
}


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

export interface SettingsCpu {
    enabled: boolean;
    maxUsage: number;
    historySize: number;
}

export interface SettingsDisks {
    enabled: boolean;
    maxUsage: number;
    mounts: string[];
}

export interface SettingsMemory {
    enabled: boolean;
    maxUsage: number;
    historySize: number;
}

export interface SettingsProcess {
    enabled: boolean;
    monitoring: string[];
}

export interface SettingsPing {
    enabled: boolean;
    monitoring: string[];
}

export interface SettingsHandShake {
    enabled: boolean;
    monitoring: string[];
    maxHandshakeTime: number;
}

export interface SettingUpdate {
    enabled: boolean;
}

export interface SettingsNetwork {
    enabled: boolean;
    historySize: number;
}

export interface Settings {
    identity: string;
    monitorTime: number;
    debounceTime: number;
    port: number;
    teamsHook: null;
    cpu: SettingsCpu;
    disk: SettingsDisks;
    memory: SettingsMemory;
    process: SettingsProcess;
    ping: SettingsPing;
    handshake: SettingsHandShake;
    updates: SettingUpdate;
    network: SettingsNetwork;
}