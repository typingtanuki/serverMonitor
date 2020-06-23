export function span(content?: Node): HTMLSpanElement {
    const out: HTMLSpanElement = document.createElement("span");
    if (content !== undefined) {
        out.appendChild(content);
    }
    return out;
}

export function div(content?: Node): HTMLDivElement {
    var out: HTMLDivElement = document.createElement("div");
    if (content !== undefined) {
        out.appendChild(content);
    }
    return out;
}

export function ul(content?: Node): HTMLUListElement {
    var out: HTMLUListElement = document.createElement("ul");
    if (content !== undefined) {
        out.appendChild(content);
    }
    return out;
}

export function li(content?: Node): HTMLLIElement {
    const out: HTMLLIElement = document.createElement("li");
    if (content !== undefined) {
        out.appendChild(content);
    }
    return out;
}

export function text(content?: string): Node {
    return document.createTextNode(content);
}

export function clearElement(element: Element): void {
    while (element.hasChildNodes()) {
        element.removeChild(element.firstChild);
    }
}

export function isAttached(element: Node): boolean {
    if (element.parentElement) {
        return isAttached(element.parentElement);
    }
    if (element.parentNode) {
        return isAttached(element.parentNode);
    }
    if ((<ShadowRoot>element).host) {
        return isAttached((<ShadowRoot>element).host);
    }

    return String(element) === "[object HTMLDocument]";
}