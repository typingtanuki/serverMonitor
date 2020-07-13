import options from "../../node_modules/css.gg/icons/svg/options.svg";
import eye from "../../node_modules/css.gg/icons/svg/eye.svg";
import smartphoneChip from "../../node_modules/css.gg/icons/svg/smartphone-chip.svg";
import drive from "../../node_modules/css.gg/icons/svg/drive.svg";
import smartphoneRam from "../../node_modules/css.gg/icons/svg/smartphone-ram.svg";
import compress from "../../node_modules/css.gg/icons/svg/compress.svg";
import ethernet from "../../node_modules/css.gg/icons/svg/ethernet.svg";
import repeat from "../../node_modules/css.gg/icons/svg/repeat.svg";
import clapperBoard from "../../node_modules/css.gg/icons/svg/clapper-board.svg";
import database from "../../node_modules/css.gg/icons/svg/database.svg";
import danger from "../../node_modules/css.gg/icons/svg/danger.svg";
import bim from "../../node_modules/css.gg/icons/svg/danger.svg";

export const iconSettings: string = options;
export const iconCollapse: string = eye;
export const iconCpu: string = smartphoneChip;
export const iconDisk: string = drive;
export const iconMemory: string = smartphoneRam;
export const iconHandshake: string = compress;
export const iconNetwork: string = ethernet;
export const iconPing: string = repeat;
export const iconProcess: string = clapperBoard;
export const iconServer: string = database;
export const iconUpdates: string = danger;
export const iconBim: string = bim;

export class Icon {
    private element: HTMLElement;

    constructor(svg: string) {
        const parser = new DOMParser();
        const doc = parser.parseFromString(svg, "image/svg+xml");

        this.element = doc.documentElement;
    }

    public build(): HTMLElement {
        return <HTMLElement>this.element.cloneNode(true);
    }
}

export function icon(icon: string): Icon {
    if (window.iconCache === undefined) {
        window.iconCache = {};
    }

    if (window.iconCache.hasOwnProperty(icon)) {
        return window.iconCache[icon];
    }

    const instance: Icon = new Icon(icon);
    window.iconCache[icon] = instance;
    return instance;
}

declare global {
    interface Window {
        iconCache: { [id: string]: Icon };
    }
}