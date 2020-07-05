export class ReportConstants {
    public static get SERVER(): string {
        return "Server";
    }

    public static get CAUSE(): string {
        return "Cause";
    }

    public static get REQUEST_TIME(): string {
        return "Request time";
    }

    public static get RESPONSE_TIME(): string {
        return "Response time";
    }

    public static get DELTA(): string {
        return "Delta";
    }

    public static get MAX_DELTA(): string {
        return "Max delta";
    }

    public static get REASON(): string {
        return "Reason";
    }

    public static get LAST_SEEN(): string {
        return "Last Seen";
    }

    public static get METHOD(): string {
        return "Method";
    }

    public static get HISTORY(): string {
        return "History";
    }

    public static get MEMORY_TOTAL(): string {
        return "Total memory";
    }

    public static get MEMORY_FREE(): string {
        return "Free memory";
    }

    public static get USAGE_MAX(): string {
        return "Maximum Usage";
    }

    public static get USAGE_CURRENT(): string {
        return "Current Usage";
    }

    public static get PROCESS(): string {
        return "Process";
    }

    public static get PID(): string {
        return "PID";
    }

    public static get UPTIME(): string {
        return "Uptime";
    }

    public static get NAME(): string {
        return "Name";
    }

    public static get COMMAND_LINE(): string {
        return "Command Line";
    }

    public static get DISK(): string {
        return "Disk";
    }

    public static get SPACE_FREE(): string {
        return "Free space";
    }

    public static get SPACE_TOTAL(): string {
        return "Total space";
    }

    public static get UPDATES(): string {
        return "Updates";
    }

    public static get INTERVAL(): string {
        return "Interval";
    }

    public static get BYTES_SENT(): string {
        return "Bytes sent";
    }

    public static get BYTES_RECEIVED(): string {
        return "Bytes received";
    }

    public static get HISTORY_RECEIVED(): string {
        return "History Received";
    }

    public static get HISTORY_SENT(): string {
        return "History Sent";
    }

    public static get TOP(): string {
        return "Top";
    }

    public static get ACTION_UPDATE(): string {
        return "Do Update";
    }
}

export interface HistoryDetail extends Detail {
    values: string[];
    dates: string[];
    max: string;
    limit: string;

}

export interface Detail {
    [key: string]: any;
}

export enum MonitorType {
    cpu = "cpu",
    memory = "memory",
    disk = "disk",
    handshake = "handshake",
    ping = "ping",
    update = "update",
    network = "network",
    server = "server",
    process = "process"
}

export interface Report {
    title: string;
    type?: MonitorType;
    details: { [id: string]: Detail };
}


export interface ServerState {
    [key: string]: boolean;
}

export interface ServerStates {
    clusterStatus: { [id: string]: ServerState };
    advanced?: { [id: string]: SuccessFailureDetail };
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
    name: MonitorType;
    state: boolean;
    advanced: Detail[];
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