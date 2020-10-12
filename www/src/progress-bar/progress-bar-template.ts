import {html, TemplateResult} from "lit-element";
import {ProgressBar} from "./progress-bar";
import {iconForType} from "../rest/types";

export function progressBarTemplate(progress: ProgressBar): TemplateResult {
    const percent: number = Math.ceil(
        (progress.current - progress.min) /
        (progress.max - progress.min));
    const warn: boolean = percent > progress.warn;
    return html`
<div class="core ${progress.label && progress.label.length > 0 ?
        "labelled" :
        ""}" title="${percent}% ${progress.label}">
    <icon-svg .icon="${iconForType(progress.type)}"></icon-svg>
    <div class="label" title="${progress.label}">${progress.label}</div>
    <div class="body ${warn ? "warn" : ""}">
        <div class="cursor" style="right:${100 - percent}%"></div>
        <div class="tick" style="right:${100 - progress.warn}%"></div>
    </div>
</div>`;
}