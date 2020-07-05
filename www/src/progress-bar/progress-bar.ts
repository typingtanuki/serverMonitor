import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from 'lit-element';
import progressStyle from "./progress-bar.less";
import {MonitorType} from "../rest/types";

@customElement('progress-bar')
export class ProgressBar extends LitElement {

    public static get styles(): CSSResult {
        return unsafeCSS(progressStyle);
    }

    static get properties() {
        return {
            type: {type: String},
            label: {type: String},
            min: {type: Number},
            max: {type: Number},
            current: {type: Number},
            warn: {type: Number}
        };
    }

    public type: MonitorType;

    public min: number;
    public max: number;
    public current: number;
    public warn: number;
    public label: string = "";

    constructor() {
        super();
    }

    public render(): TemplateResult {
        const percent: number = Math.ceil(this.current - this.min / this.max - this.min);
        const warn: boolean = percent > this.warn;
        return html`
<div class="core ${this.label && this.label.length > 0 ? "labelled" : ""}" title="${percent}% ${this.label}">
    <div class="icon ${this.iconName()}"></div>
    <div class="label" title="${this.label}">${this.label}</div>
    <div class="body ${warn ? "warn" : ""}"><div class="cursor" style="right:${100 - percent}%"></div></div>
</div>`;
    }

    private iconName(): string {
        switch (this.type) {
            case MonitorType.cpu:
                return "gg-smartphone-chip";
            case MonitorType.disk:
                return "gg-drive";
            case MonitorType.memory:
                return "gg-smartphone-ram";
        }
        return "";
    }
}