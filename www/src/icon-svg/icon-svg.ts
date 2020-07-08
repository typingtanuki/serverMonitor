import {CSSResult, customElement, html, LitElement, property, TemplateResult, unsafeCSS} from 'lit-element';
import iconStyle from "./icon-svg.less";
import iconOptions from "../../node_modules/css.gg/icons/svg/options.svg";
import iconEye from "../../node_modules/css.gg/icons/svg/eye.svg";
import iconCpu from "../../node_modules/css.gg/icons/svg/smartphone-chip.svg";
import iconDrive from "../../node_modules/css.gg/icons/svg/drive.svg";
import iconMemory from "../../node_modules/css.gg/icons/svg/smartphone-ram.svg";
import iconHandshake from "../../node_modules/css.gg/icons/svg/compress.svg";
import iconNetwork from "../../node_modules/css.gg/icons/svg/ethernet.svg";
import iconPing from "../../node_modules/css.gg/icons/svg/repeat.svg";
import iconProcess from "../../node_modules/css.gg/icons/svg/clapper-board.svg";
import iconServer from "../../node_modules/css.gg/icons/svg/database.svg";
import iconUpdate from "../../node_modules/css.gg/icons/svg/danger.svg";
import {MonitorType} from "../rest/types";

@customElement('icon-svg')
export class IconSvg extends LitElement {
    @property() public icon: IconType | null = null;

    constructor(icon: IconType) {
        super();
        this.icon = icon;
    }

    public static get styles(): CSSResult {
        return unsafeCSS(iconStyle);
    }

    public render(): TemplateResult {
        return html`${this.iconSvg()}`;
    }

    private iconSvg(): string | HTMLElement {
        let resolved: string | null = null;
        switch (this.icon) {
            case IconType.options:
                resolved = iconOptions;
                break;
            case IconType.eye:
                resolved = iconEye;
                break;
            case IconType.cpu:
                resolved = iconCpu;
                break;
            case IconType.disk:
                resolved = iconDrive
                break;
            case IconType.memory:
                resolved = iconMemory;
                break;
            case IconType.handshake:
                resolved = iconHandshake;
                break;
            case IconType.network:
                resolved = iconNetwork;
                break;
            case IconType.ping:
                resolved = iconPing;
                break;
            case IconType.process:
                resolved = iconProcess;
                break;
            case IconType.server:
                resolved = iconServer;
                break;
            case IconType.update:
                resolved = iconUpdate;
                break;
        }
        if (resolved === null) {
            return "--";
        }
        const parser = new DOMParser();
        const doc = parser.parseFromString(resolved, "image/svg+xml");
        return doc.documentElement;
    }
}

export enum IconType {
    options = "options",
    eye = "eye",
    cpu = "cpu",
    disk = "drive",
    memory = "memory",
    handshake = "handshake",
    network = "network",
    ping = "ping",
    process = "process",
    server = "server",
    update = "update"
}

export function iconForType(type: MonitorType): IconType {
    switch (type) {
        case MonitorType.cpu:
            return IconType.cpu;
        case MonitorType.disk:
            return IconType.disk;
        case MonitorType.memory:
            return IconType.memory;
        case MonitorType.handshake:
            return IconType.handshake;
        case MonitorType.network:
            return IconType.network;
        case MonitorType.ping:
            return IconType.ping;
        case MonitorType.process:
            return IconType.process;
        case MonitorType.server:
            return IconType.server;
        case MonitorType.update:
            return IconType.update;
    }
    return IconType.eye;
}
