import {CSSResult, customElement, html, LitElement, TemplateResult, unsafeCSS} from 'lit-element';
import progressStyle from "./progress-bar.less";
import {MonitorType} from "../rest/types";
import {iconForType, IconType} from "../icon-svg/icon-svg";

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
        const icon: IconType = iconForType(this.type);
        return html`
<div class="core ${this.label && this.label.length > 0 ?
            "labelled" :
            ""}" title="${percent}% ${this.label}">
    <icon-svg icon="${icon}"></icon-svg>
    <div class="label" title="${this.label}">${this.label}</div>
    <div class="body ${warn ? "warn" : ""}">
        <div class="cursor" style="right:${100 - percent}%"></div>
        <div class="tick" style="right:${100 - this.warn}%"></div>
    </div>
</div>`;
    }
}