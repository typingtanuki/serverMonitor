import {DetailView} from "./detail-view";
import {html, TemplateResult} from "lit-element";
import {DataType, formCheckbox, formSection, formText} from "./form-manager";

export function detailViewTemplate(element: DetailView): TemplateResult {
    return html`
    <div id="server">${element.server.name}</div>
    <div id="response">
        <settings-button label="Edit Settings" @click="${element.showSettings}"></settings-button>
        <report-list class="failure" .reports="${element.failure}"></report-list>
        <report-list class="success" .reports="${element.success}"></report-list>
    </div>`;
}

export function settingsViewTemplate(element: DetailView): TemplateResult {
    return html`
    <div id="server">${element.server.name}</div>
    <div id="response"><table class="settings">
        ${formSection("Global")}
        ${formText("Name", element.settings, "identity", DataType.string)}
        ${formText("Monitor time", element.settings, "monitorTime", DataType.number)}
        ${formText("Debounce time", element.settings, "debounceTime", DataType.number)}
        ${formText("API port", element.settings, "port", DataType.string)}
        ${formText("Teams Hook", element.settings, "teamsHook", DataType.string)}
        
        ${formSection("CPU")}
        ${formCheckbox("Enabled", element.settings, "cpu.enabled")}
        ${formText("Warn Level", element.settings, "cpu.maxUsage", DataType.number)}
        ${formText("History Size", element.settings, "cpu.historySize", DataType.number)}
    
        ${formSection("Memory")}
        ${formCheckbox("Enabled", element.settings, "memory.enabled")}
        ${formText("Warn Level", element.settings, "memory.maxUsage", DataType.number)}
        ${formText("History Size",
        element.settings,
        "memory.historySize",
        DataType.number)}
    
        ${formSection("Network")}
        ${formCheckbox("Enabled", element.settings, "network.enabled")}
        ${formText("History Size",
        element.settings,
        "network.historySize",
        DataType.number)}
    
        ${formSection("Disks")}
        ${formCheckbox("Enabled", element.settings, "disk.enabled")}
        ${formText("Warn Level", element.settings, "disk.maxUsage", DataType.number)}
        ${formText("Disks", element.settings, "disk.mounts", DataType.array)}
    
        ${formSection("Process")}
        ${formCheckbox("Enabled", element.settings, "process.enabled")}
        ${formText("Processes", element.settings, "process.monitoring", DataType.array)}
        ${formText("History Size",
        element.settings,
        "process.historySize",
        DataType.number)}
    
        ${formSection("Ping")}
        ${formCheckbox("Enabled", element.settings, "ping.enabled")}
        ${formText("Servers", element.settings, "ping.monitoring", DataType.array)}
    
        ${formSection("Handshake")}
        ${formCheckbox("Enabled", element.settings, "handshake.enabled")}
        ${formText("Servers", element.settings, "handshake.monitoring", DataType.array)}
        ${formText("Timeout",
        element.settings,
        "handshake.maxHandshakeTime",
        DataType.number)}
    
        ${formSection("System Updates")}
        ${formCheckbox("Enabled", element.settings, "updates.enabled")}
    </table></div>
        <settings-button label="Save Settings" @click="${element.saveSettings}"></settings-button>`;
}