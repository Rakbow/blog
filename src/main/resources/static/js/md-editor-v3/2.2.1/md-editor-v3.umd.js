(function (r, c) {
    typeof exports == "object" && typeof module != "undefined" ? module.exports = c(require("vue")) : typeof define == "function" && define.amd ? define(["vue"], c) : (r = typeof globalThis != "undefined" ? globalThis : r || self, r.MdEditorV3 = c(r.Vue))
})(this, function (r) {
    "use strict";
    var Kn = Object.defineProperty, Zn = Object.defineProperties;
    var Wn = Object.getOwnPropertyDescriptors;
    var gt = Object.getOwnPropertySymbols;
    var Gn = Object.prototype.hasOwnProperty, Qn = Object.prototype.propertyIsEnumerable;
    var pt = (r, c, W) => c in r ? Kn(r, c, {enumerable: !0, configurable: !0, writable: !0, value: W}) : r[c] = W,
        Y = (r, c) => {
            for (var W in c || (c = {})) Gn.call(c, W) && pt(r, W, c[W]);
            if (gt) for (var W of gt(c)) Qn.call(c, W) && pt(r, W, c[W]);
            return r
        }, Oe = (r, c) => Zn(r, Wn(c));
    const c = "md", W = "https://at.alicdn.com/t/font_2605852_pqekijay2ij.js",
        H = "https://cdnjs.cloudflare.com/ajax/libs", kt = `${H}/highlight.js/11.5.1/highlight.min.js`,
        Me = {main: `${H}/prettier/2.4.0/standalone.js`, markdown: `${H}/prettier/2.4.0/parser-markdown.js`},
        Be = {css: `${H}/cropperjs/1.5.12/cropper.min.css`, js: `${H}/cropperjs/1.5.12/cropper.min.js`},
        bt = `${H}/screenfull.js/5.1.0/screenfull.min.js`,
        Ue = ["bold", "underline", "italic", "strikeThrough", "-", "title", "sub", "sup", "quote", "unorderedList", "orderedList", "-", "codeRow", "code", "link", "image", "table", "mermaid", "katex", "-", "revoke", "next", "save", "=", "prettier", "pageFullscreen", "fullscreen", "preview", "htmlPreview", "catalog", "github"],
        qe = ["markdownTotal", "=", "scrollSwitch"], Ke = {
            "zh-CN": {
                toolbarTips: {
                    bold: "\u52A0\u7C97",
                    underline: "\u4E0B\u5212\u7EBF",
                    italic: "\u659C\u4F53",
                    strikeThrough: "\u5220\u9664\u7EBF",
                    title: "\u6807\u9898",
                    sub: "\u4E0B\u6807",
                    sup: "\u4E0A\u6807",
                    quote: "\u5F15\u7528",
                    unorderedList: "\u65E0\u5E8F\u5217\u8868",
                    orderedList: "\u6709\u5E8F\u5217\u8868",
                    codeRow: "\u884C\u5185\u4EE3\u7801",
                    code: "\u5757\u7EA7\u4EE3\u7801",
                    link: "\u94FE\u63A5",
                    image: "\u56FE\u7247",
                    table: "\u8868\u683C",
                    mermaid: "mermaid\u56FE",
                    katex: "katex\u516C\u5F0F",
                    revoke: "\u540E\u9000",
                    next: "\u524D\u8FDB",
                    save: "\u4FDD\u5B58",
                    prettier: "\u7F8E\u5316",
                    pageFullscreen: "\u6D4F\u89C8\u5668\u5168\u5C4F",
                    fullscreen: "\u5C4F\u5E55\u5168\u5C4F",
                    preview: "\u9884\u89C8",
                    htmlPreview: "html\u4EE3\u7801\u9884\u89C8",
                    catalog: "\u76EE\u5F55",
                    github: "\u6E90\u7801\u5730\u5740"
                },
                titleItem: {
                    h1: "\u4E00\u7EA7\u6807\u9898",
                    h2: "\u4E8C\u7EA7\u6807\u9898",
                    h3: "\u4E09\u7EA7\u6807\u9898",
                    h4: "\u56DB\u7EA7\u6807\u9898",
                    h5: "\u4E94\u7EA7\u6807\u9898",
                    h6: "\u516D\u7EA7\u6807\u9898"
                },
                imgTitleItem: {
                    link: "\u6DFB\u52A0\u94FE\u63A5",
                    upload: "\u4E0A\u4F20\u56FE\u7247",
                    clip2upload: "\u88C1\u526A\u4E0A\u4F20"
                },
                linkModalTips: {
                    title: "\u6DFB\u52A0",
                    descLable: "\u94FE\u63A5\u63CF\u8FF0\uFF1A",
                    descLablePlaceHolder: "\u8BF7\u8F93\u5165\u63CF\u8FF0...",
                    urlLable: "\u94FE\u63A5\u5730\u5740\uFF1A",
                    UrlLablePlaceHolder: "\u8BF7\u8F93\u5165\u94FE\u63A5...",
                    buttonOK: "\u786E\u5B9A"
                },
                clipModalTips: {title: "\u88C1\u526A\u56FE\u7247\u4E0A\u4F20", buttonUpload: "\u4E0A\u4F20"},
                copyCode: {
                    text: "\u590D\u5236\u4EE3\u7801",
                    successTips: "\u5DF2\u590D\u5236\uFF01",
                    failTips: "\u590D\u5236\u5931\u8D25\uFF01"
                },
                mermaid: {
                    flow: "\u6D41\u7A0B\u56FE",
                    sequence: "\u65F6\u5E8F\u56FE",
                    gantt: "\u7518\u7279\u56FE",
                    class: "\u7C7B\u56FE",
                    state: "\u72B6\u6001\u56FE",
                    pie: "\u997C\u56FE",
                    relationship: "\u5173\u7CFB\u56FE",
                    journey: "\u65C5\u7A0B\u56FE"
                },
                katex: {inline: "\u884C\u5185\u516C\u5F0F", block: "\u5757\u7EA7\u516C\u5F0F"},
                footer: {markdownTotal: "\u5B57\u6570", scrollAuto: "\u540C\u6B65\u6EDA\u52A8"}
            }, "en-US": {
                toolbarTips: {
                    bold: "bold",
                    underline: "underline",
                    italic: "italic",
                    strikeThrough: "strikeThrough",
                    title: "title",
                    sub: "subscript",
                    sup: "superscript",
                    quote: "quote",
                    unorderedList: "unordered list",
                    orderedList: "ordered list",
                    codeRow: "inline code",
                    code: "block-level code",
                    link: "link",
                    image: "image",
                    table: "table",
                    mermaid: "mermaid",
                    katex: "formula",
                    revoke: "revoke",
                    next: "undo revoke",
                    save: "save",
                    prettier: "prettier",
                    pageFullscreen: "fullscreen in page",
                    fullscreen: "fullscreen",
                    preview: "preview",
                    htmlPreview: "html preview",
                    catalog: "catalog",
                    github: "source code"
                },
                titleItem: {
                    h1: "Lv1 Heading",
                    h2: "Lv2 Heading",
                    h3: "Lv3 Heading",
                    h4: "Lv4 Heading",
                    h5: "Lv5 Heading",
                    h6: "Lv6 Heading"
                },
                imgTitleItem: {link: "Add Img Link", upload: "Upload Img", clip2upload: "Clip Upload"},
                linkModalTips: {
                    title: "Add ",
                    descLable: "Desc:",
                    descLablePlaceHolder: "Enter a description...",
                    urlLable: "Link:",
                    UrlLablePlaceHolder: "Enter a link...",
                    buttonOK: "OK"
                },
                clipModalTips: {title: "Crop Image", buttonUpload: "Upload"},
                copyCode: {text: "Copy", successTips: "Copied!", failTips: "Copy failed!"},
                mermaid: {
                    flow: "flow",
                    sequence: "sequence",
                    gantt: "gantt",
                    class: "class",
                    state: "state",
                    pie: "pie",
                    relationship: "relationship",
                    journey: "journey"
                },
                katex: {inline: "inline", block: "block"},
                footer: {markdownTotal: "Word Count", scrollAuto: "Scroll Auto"}
            }
        }, yt = `${H}/mermaid/8.13.5/mermaid.min.js`,
        Ze = {js: `${H}/KaTeX/0.15.1/katex.min.js`, css: `${H}/KaTeX/0.15.1/katex.min.css`}, We = {
            a11y: {
                light: `${H}/highlight.js/11.5.1/styles/a11y-light.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/a11y-dark.min.css`
            },
            atom: {
                light: `${H}/highlight.js/11.5.1/styles/atom-one-light.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/atom-one-dark.min.css`
            },
            github: {
                light: `${H}/highlight.js/11.5.1/styles/github.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/github-dark.min.css`
            },
            gradient: {
                light: `${H}/highlight.js/11.5.1/styles/gradient-light.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/gradient-dark.min.css`
            },
            kimbie: {
                light: `${H}/highlight.js/11.5.1/styles/kimbie-light.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/kimbie-dark.min.css`
            },
            paraiso: {
                light: `${H}/highlight.js/11.5.1/styles/paraiso-light.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/paraiso-dark.min.css`
            },
            qtcreator: {
                light: `${H}/highlight.js/11.5.1/styles/qtcreator-light.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/qtcreator-dark.min.css`
            },
            stackoverflow: {
                light: `${H}/highlight.js/11.5.1/styles/stackoverflow-light.min.css`,
                dark: `${H}/highlight.js/11.5.1/styles/stackoverflow-dark.min.css`
            }
        }, M = {markedRenderer: i => i, markedExtensions: [], markedOptions: {}, editorExtensions: {}, editorConfig: {}},
        wt = i => {
            if (i) for (const t in i) {
                const e = i[t];
                e && (M[t] = e)
            }
        };

    class vt {
        constructor() {
            this.pools = {}
        }

        remove(t, e, n) {
            const o = this.pools[t] && this.pools[t][e];
            o && (this.pools[t][e] = o.filter(a => a === n))
        }

        clear(t) {
            this.pools[t] = {}
        }

        on(t, e) {
            return this.pools[t] || (this.pools[t] = {}), this.pools[t][e.name] || (this.pools[t][e.name] = []), this.pools[t][e.name].push(e.callback), this.pools[t][e.name].includes(e.callback)
        }

        emit(t, e, ...n) {
            this.pools[t] || (this.pools[t] = {});
            const o = this.pools[t][e];
            o && o.forEach(a => {
                try {
                    a(...n)
                } catch (s) {
                    console.error(`${e} monitor event exception\uFF01`, s)
                }
            })
        }
    }

    var C = new vt;
    const J = (i, t = 0, e = t) => new Promise((n, l) => {
        i.setSelectionRange ? setTimeout(() => {
            i.setSelectionRange(t, e), i.focus(), n(!0)
        }, 0) : (console.error("Can not reset position!"), l())
    }), Fe = (i, t, e) => {
        const {deviationStart: n = 0, deviationEnd: l = 0, direct: o = !1, select: a = !1} = e;
        let s = "";
        if (i.selectionStart || i.selectionStart === 0) {
            const d = i.selectionStart,
                h = i.selectionEnd || 0, {prefixVal: u = i.value.substring(0, d), subfixVal: g = i.value.substring(h, i.value.length)} = e;
            s = u + t + g, J(i, a ? d + n : d + t.length + l, d + t.length + l)
        } else s += t;
        return o && (i.value = s), s
    }, xt = (i, t = {newWindow: !0, nofollow: !0}) => {
        i || console.error("error link!");
        const e = document.createElement("a");
        e.href = i, e.style.display = "none", t.newWindow && (e.target = "_blank"), t.nofollow && (e.rel = "noopener noreferrer"), document.body.appendChild(e), e.click(), document.body.removeChild(e)
    }, Ge = (i, t) => {
        const e = pe(() => {
            i.removeEventListener("scroll", n), i.addEventListener("scroll", n), t.removeEventListener("scroll", n), t.addEventListener("scroll", n)
        }, 50), n = l => {
            const o = i.clientHeight, a = t.clientHeight, s = i.scrollHeight, d = t.scrollHeight, h = (s - o) / (d - a);
            l.target === i ? (t.removeEventListener("scroll", n), t.scrollTo({top: i.scrollTop / h}), e()) : (i.removeEventListener("scroll", n), i.scrollTo({top: t.scrollTop * h}), e())
        };
        return [e, () => {
            i.removeEventListener("scroll", n), t.removeEventListener("scroll", n)
        }]
    }, $t = (i, t = "image.png") => {
        const e = i.split(","), n = e[0].match(/:(.*?);/);
        if (n) {
            const l = n[1], o = atob(e[1]);
            let a = o.length;
            const s = new Uint8Array(a);
            for (; a--;) s[a] = o.charCodeAt(a);
            return new File([s], t, {type: l})
        }
        return null
    }, Qe = i => {
        if (!i.trim()) return i;
        const t = i.split(`
`), e = ['<span rn-wrapper aria-hidden="true">'];
        return t.forEach(() => {
            e.push("<span></span>")
        }), e.push("</span>"), `<span class="code-block">${i}</span>${e.join("")}`
    }, pe = (i, t = 200) => {
        let e = 0;
        return (...n) => {
            e && clearTimeout(e), e = window.setTimeout(() => {
                i.apply(globalThis, n), e = 0
            }, t)
        }
    }, Xe = (i, t = "$") => {
        const e = i.split(t);
        let n = t, l = "";
        for (let o = 1; o < e.length; o++) if (/\\$/.test(e[o])) n += e[o] + "$", l += e[o] + "$"; else {
            n += e[o] + t, l += e[o];
            break
        }
        return [n, l]
    }, Ct = i => {
        var e;
        return navigator.userAgent.indexOf("Firefox") > -1 ? i.value.substring(i.selectionStart, i.selectionEnd) : ((e = window.getSelection()) == null ? void 0 : e.toString()) || ""
    }, Ye = (i, t) => {
        const e = n => {
            const l = i.parentElement || document.body, o = l.offsetWidth,
                a = l.offsetHeight, {clientWidth: s} = document.documentElement, {clientHeight: d} = document.documentElement,
                h = n.offsetX, u = n.offsetY, g = f => {
                    let $ = f.x + document.body.scrollLeft - document.body.clientLeft - h,
                        v = f.y + document.body.scrollTop - document.body.clientTop - u;
                    $ = $ < 1 ? 1 : $ < s - o - 1 ? $ : s - o - 1, v = v < 1 ? 1 : v < d - a - 1 ? v : d - a - 1, t ? t($, v) : (l.style.left = `${$}px`, l.style.top = `${v}px`)
                };
            document.addEventListener("mousemove", g);
            const k = () => {
                document.removeEventListener("mousemove", g), document.removeEventListener("mouseup", k)
            };
            document.addEventListener("mouseup", k)
        };
        return i.addEventListener("mousedown", e), () => {
            i.removeEventListener("mousedown", e)
        }
    }, G = (i, t = "") => {
        const e = document.getElementById(i.id);
        e ? t !== "" && i.onload instanceof Function && (Reflect.get(window, t) ? i.onload(new Event("load")) : e.addEventListener("load", i.onload)) : document.head.appendChild(i)
    }, St = pe((i, t, e) => {
        const n = document.getElementById(i);
        n && n.setAttribute(t, e)
    }, 10), Et = (i, t) => {
        const {editorId: e, noPrettier: n} = i, l = a => {
            var s, d;
            return ((s = i.toolbars) == null ? void 0 : s.includes(a)) && !((d = i.toolbarsExclude) != null && d.includes(a)) && !n
        }, o = a => {
            if (a.target === document.querySelector(`#${i.editorId}-textarea`)) if (C.emit(e, "selectTextChange"), a.ctrlKey || a.metaKey) switch (a.code) {
                case "KeyS": {
                    a.shiftKey ? l("strikeThrough") && C.emit(e, "replace", "strikeThrough") : l("save") && (C.emit(e, "onSave", i.modelValue), a.preventDefault());
                    break
                }
                case "KeyB": {
                    l("bold") && (C.emit(e, "replace", "bold"), a.preventDefault());
                    break
                }
                case "KeyU": {
                    a.shiftKey ? l("unorderedList") && (C.emit(e, "replace", "unorderedList"), a.preventDefault()) : l("underline") && (C.emit(e, "replace", "underline"), a.preventDefault());
                    break
                }
                case "KeyI": {
                    a.shiftKey ? l("image") && (C.emit(e, "openModals", "image"), a.preventDefault()) : l("italic") && (C.emit(e, "replace", "italic"), a.preventDefault());
                    break
                }
                case "Digit1": {
                    l("title") && (C.emit(e, "replace", "h1"), a.preventDefault());
                    break
                }
                case "Digit2": {
                    l("title") && (C.emit(e, "replace", "h2"), a.preventDefault());
                    break
                }
                case "Digit3": {
                    l("title") && (C.emit(e, "replace", "h3"), a.preventDefault());
                    break
                }
                case "Digit4": {
                    l("title") && (C.emit(e, "replace", "h4"), a.preventDefault());
                    break
                }
                case "Digit5": {
                    l("title") && (C.emit(e, "replace", "h5"), a.preventDefault());
                    break
                }
                case "Digit6": {
                    l("title") && (C.emit(e, "replace", "h6"), a.preventDefault());
                    break
                }
                case "ArrowUp": {
                    l("sup") && (C.emit(e, "replace", "sup"), a.preventDefault());
                    break
                }
                case "ArrowDown": {
                    l("sub") && (C.emit(e, "replace", "sub"), a.preventDefault());
                    break
                }
                case "KeyQ": {
                    if (a.key === "a") {
                        a.target.select();
                        return
                    }
                    C.emit(e, "replace", "quote"), a.preventDefault();
                    break
                }
                case "KeyA":
                    if (a.key === "q") {
                        C.emit(e, "replace", "quote"), a.preventDefault();
                        break
                    } else return;
                case "KeyO": {
                    l("orderedList") && (C.emit(e, "replace", "orderedList"), a.preventDefault());
                    break
                }
                case "KeyC": {
                    if (a.shiftKey) l("code") && (C.emit(e, "replace", "code"), a.preventDefault()); else if (a.altKey) l("codeRow") && (C.emit(e, "replace", "codeRow"), a.preventDefault()); else {
                        a.preventDefault(), C.emit(e, "replace", "ctrlC");
                        break
                    }
                    break
                }
                case "KeyL": {
                    l("link") && (C.emit(e, "openModals", "link"), a.preventDefault());
                    break
                }
                case "KeyZ": {
                    if (a.key === "w") return;
                    a.shiftKey ? l("next") && (C.emit(e, "ctrlShiftZ"), a.preventDefault()) : l("revoke") && (C.emit(e, "ctrlZ"), a.preventDefault());
                    break
                }
                case "KeyW":
                    if (a.key === "z") {
                        a.shiftKey ? l("next") && (C.emit(e, "ctrlShiftZ"), a.preventDefault()) : l("revoke") && (C.emit(e, "ctrlZ"), a.preventDefault());
                        break
                    } else return;
                case "KeyF": {
                    a.shiftKey && l("prettier") && (C.emit(e, "replace", "prettier"), a.preventDefault());
                    break
                }
                case "KeyT": {
                    a.altKey && a.shiftKey && l("table") && (C.emit(e, "replace", "table"), a.preventDefault());
                    break
                }
                case "KeyX": {
                    C.emit(e, "replace", "ctrlX"), a.preventDefault();
                    break
                }
                case "KeyD": {
                    a.preventDefault(), C.emit(e, "replace", "ctrlD");
                    break
                }
            } else a.code === "Tab" && (a.preventDefault(), a.shiftKey ? C.emit(e, "replace", "shiftTab") : C.emit(e, "replace", "tab"))
        };
        r.onMounted(() => {
            i.previewOnly || (window.addEventListener("keydown", o), C.on(e, {
                name: "onSave", callback() {
                    i.onSave ? i.onSave(i.modelValue) : t.emit("onSave", i.modelValue)
                }
            }))
        }), r.onBeforeUnmount(() => {
            i.previewOnly || window.removeEventListener("keydown", o)
        })
    }, Nt = i => {
        var l;
        const {editorId: t} = i, e = (l = M == null ? void 0 : M.editorExtensions) == null ? void 0 : l.highlight;
        r.provide("editorId", t), r.provide("tabWidth", i.tabWidth), r.provide("theme", r.computed(() => i.theme)), r.provide("highlight", r.computed(() => {
            const o = Y(Y({}, We), e == null ? void 0 : e.css);
            return {
                js: (e == null ? void 0 : e.js) || kt,
                css: o[i.codeTheme] ? o[i.codeTheme][i.theme] : We.atom[i.theme]
            }
        })), r.provide("historyLength", i.historyLength), r.provide("previewOnly", i.previewOnly), r.provide("showCodeRowNumber", i.showCodeRowNumber);
        const n = r.computed(() => {
            var a;
            const o = Y(Y({}, Ke), (a = M == null ? void 0 : M.editorConfig) == null ? void 0 : a.languageUserDefined);
            return o[i.language] ? o[i.language] : Ke["zh-CN"]
        });
        r.provide("usedLanguageText", n), r.provide("previewTheme", r.computed(() => i.previewTheme))
    }, Tt = i => {
        var d, h, u, g, k, f;
        const {noPrettier: t, previewOnly: e, noIconfont: n} = i, {editorExtensions: l} = M,
            o = t || !!((h = (d = M.editorExtensions) == null ? void 0 : d.prettier) != null && h.prettierInstance),
            a = t || !!((g = (u = M.editorExtensions) == null ? void 0 : u.prettier) != null && g.parserMarkdownInstance),
            s = !!((f = (k = M.editorExtensions) == null ? void 0 : k.cropper) != null && f.instance);
        r.onMounted(() => {
            var E, L, F, O;
            const $ = document.createElement("script");
            $.src = (l == null ? void 0 : l.iconfont) || W, $.id = `${c}-icon`;
            const v = document.createElement("script"), w = document.createElement("script");
            v.src = ((E = l == null ? void 0 : l.prettier) == null ? void 0 : E.standaloneJs) || Me.main, v.id = `${c}-prettier`, w.src = ((L = l == null ? void 0 : l.prettier) == null ? void 0 : L.parserMarkdownJs) || Me.markdown, w.id = `${c}-prettierMD`;
            const b = document.createElement("link");
            b.rel = "stylesheet", b.href = ((F = l == null ? void 0 : l.cropper) == null ? void 0 : F.css) || Be.css, b.id = `${c}-cropperCss`;
            const y = document.createElement("script");
            y.src = ((O = l == null ? void 0 : l.cropper) == null ? void 0 : O.js) || Be.js, y.id = `${c}-cropper`, e || (n || G($), s || (G(b), G(y)), o || G(v), a || G(w))
        })
    }, Vt = (i, t) => {
        const {editorId: e} = i, n = r.reactive({
            pageFullScreen: i.pageFullScreen,
            fullscreen: !1,
            preview: i.preview,
            htmlPreview: i.preview ? !1 : i.htmlPreview
        }), l = (s, d) => {
            n[d] = s, d === "preview" && n.preview ? n.htmlPreview = !1 : d === "htmlPreview" && n.htmlPreview && (n.preview = !1)
        };
        let o = "";
        const a = () => {
            n.pageFullScreen || n.fullscreen ? document.body.style.overflow = "hidden" : document.body.style.overflow = o
        };
        return r.watch(() => [n.pageFullScreen, n.fullscreen], a), r.onMounted(() => {
            i.previewOnly || C.on(e, {
                name: "uploadImage", callback(s, d) {
                    const h = u => {
                        C.emit(e, "replace", "image", {desc: "", urls: u}), d && d()
                    };
                    i.onUploadImg ? i.onUploadImg(s, h) : t.emit("onUploadImg", s, h)
                }
            }), o = document.body.style.overflow, a()
        }), [n, l]
    }, zt = i => {
        const {editorId: t} = i, e = r.ref(!1);
        r.onMounted(() => {
            C.on(t, {
                name: "catalogShow", callback: () => {
                    e.value = !e.value
                }
            })
        });
        const n = r.computed(() => !i.toolbarsExclude.includes("catalog") && i.toolbars.includes("catalog"));
        return [e, n]
    };
    var Xn = "", At = r.defineComponent({
        setup() {
            return () => r.createVNode("div", {class: `${c}-divider`}, null)
        }
    });
    const Q = ({instance: i, ctx: t, props: e = {}}, n = "default") => {
        const l = (i == null ? void 0 : i.$slots[n]) || (t == null ? void 0 : t.slots[n]);
        return (l ? l(i) : "") || e[n]
    };
    var Yn = "";
    const Lt = () => ({
        trigger: {type: String, default: "hover"},
        overlay: {type: [String, Object], default: ""},
        visible: {type: Boolean, default: !1},
        onChange: {
            type: Function, default: () => () => {
            }
        },
        relative: {type: String, default: "html"}
    });
    var oe = r.defineComponent({
        props: Lt(), setup(i, t) {
            const e = `${c}-dropdown-hidden`,
                n = r.reactive({overlayClass: [e], overlayStyle: {}, triggerHover: !1, overlayHover: !1}), l = r.ref(),
                o = r.ref(), a = () => {
                    var E;
                    i.trigger === "hover" && (n.triggerHover = !0);
                    const g = l.value, k = o.value, f = g.getBoundingClientRect(), $ = g.offsetTop, v = g.offsetLeft,
                        w = f.height, b = f.width,
                        y = ((E = document.querySelector(i.relative)) == null ? void 0 : E.scrollLeft) || 0;
                    n.overlayStyle = Oe(Y({}, n.overlayStyle), {
                        top: $ + w + "px",
                        left: v - k.offsetWidth / 2 + b / 2 - y + "px"
                    }), i.onChange(!0)
                }, s = () => {
                    n.overlayHover = !0
                };
            r.watch(() => i.visible, g => {
                g ? n.overlayClass = n.overlayClass.filter(k => k !== e) : n.overlayClass.push(e)
            });
            const d = g => {
                const k = l.value, f = o.value;
                !k.contains(g.target) && !f.contains(g.target) && i.onChange(!1)
            };
            let h = -1;
            const u = g => {
                l.value === g.target ? n.triggerHover = !1 : n.overlayHover = !1, clearTimeout(h), h = window.setTimeout(() => {
                    !n.overlayHover && !n.triggerHover && i.onChange(!1)
                }, 10)
            };
            return r.onMounted(() => {
                i.trigger === "click" ? (l.value.addEventListener("click", a), document.addEventListener("click", d)) : (l.value.addEventListener("mouseenter", a), l.value.addEventListener("mouseleave", u), o.value.addEventListener("mouseenter", s), o.value.addEventListener("mouseleave", u))
            }), r.onBeforeUnmount(() => {
                i.trigger === "click" ? (l.value.removeEventListener("click", a), document.removeEventListener("click", d)) : (l.value.removeEventListener("mouseenter", a), l.value.removeEventListener("mouseleave", u), o.value.removeEventListener("mouseenter", s), o.value.removeEventListener("mouseleave", u))
            }), () => {
                const g = Q({ctx: t}), k = Q({props: i, ctx: t}, "overlay"),
                    f = r.cloneVNode(g instanceof Array ? g[0] : g, {ref: l}), $ = r.createVNode("div", {
                        class: [`${c}-dropdown`, n.overlayClass],
                        style: n.overlayStyle,
                        ref: o
                    }, [r.createVNode("div", {class: `${c}-dropdown-overlay`}, [k instanceof Array ? k[0] : k])]);
                return [f, $]
            }
        }
    }), Jn = "";
    const It = () => ({
        title: {type: String, default: ""},
        visible: {type: Boolean, default: !1},
        width: {type: String, default: "auto"},
        height: {type: String, default: "auto"},
        onClose: {
            type: Function, default: () => () => {
            }
        },
        showAdjust: {type: Boolean, default: !1},
        isFullscreen: {type: Boolean, default: !1},
        onAdjust: {
            type: Function, default: () => () => {
            }
        }
    });
    var je = r.defineComponent({
        props: It(), setup(i, t) {
            const e = r.ref(i.visible), n = r.ref([`${c}-modal`]), l = r.ref(), o = r.ref();
            let a = () => {
            };
            const s = r.reactive({initPos: {left: "0px", top: "0px"}, historyPos: {left: "0px", top: "0px"}}),
                d = r.computed(() => i.isFullscreen ? {width: "100%", height: "100%"} : {
                    width: i.width,
                    height: i.height
                });
            return r.onMounted(() => {
                a = Ye(o.value, (h, u) => {
                    s.initPos.left = h + "px", s.initPos.top = u + "px"
                })
            }), r.onBeforeUnmount(() => {
                a()
            }), r.watch(() => i.isFullscreen, h => {
                h ? a() : a = Ye(o.value, (u, g) => {
                    s.initPos.left = u + "px", s.initPos.top = g + "px"
                })
            }), r.watch(() => i.visible, h => {
                h ? (n.value.push("zoom-in"), e.value = h, r.nextTick(() => {
                    const u = l.value.offsetWidth / 2, g = l.value.offsetHeight / 2,
                        k = document.documentElement.clientWidth / 2, f = document.documentElement.clientHeight / 2;
                    s.initPos.left = k - u + "px", s.initPos.top = f - g + "px"
                }), setTimeout(() => {
                    n.value = n.value.filter(u => u !== "zoom-in")
                }, 140)) : (n.value.push("zoom-out"), setTimeout(() => {
                    n.value = n.value.filter(u => u !== "zoom-out"), e.value = h
                }, 130))
            }), () => {
                const h = Q({ctx: t}), u = Q({props: i, ctx: t}, "title");
                return r.createVNode("div", {style: {display: e.value ? "block" : "none"}}, [r.createVNode("div", {
                    class: `${c}-modal-mask`,
                    onClick: i.onClose
                }, null), r.createVNode("div", {
                    class: n.value,
                    style: Y(Y({}, s.initPos), d.value),
                    ref: l
                }, [r.createVNode("div", {
                    class: `${c}-modal-header`,
                    ref: o
                }, [u || ""]), r.createVNode("div", {class: `${c}-modal-body`}, [h]), r.createVNode("div", {class: `${c}-modal-func`}, [i.showAdjust && r.createVNode("div", {
                    class: `${c}-modal-adjust`,
                    onClick: g => {
                        g.stopPropagation(), i.isFullscreen ? s.initPos = s.historyPos : (s.historyPos = s.initPos, s.initPos = {
                            left: "0",
                            top: "0"
                        }), i.onAdjust(!i.isFullscreen)
                    }
                }, [r.createVNode("svg", {
                    class: `${c}-icon`,
                    "aria-hidden": "true"
                }, [r.createVNode("use", {"xlink:href": `#icon-${i.isFullscreen ? "suoxiao" : "fangda"}`}, null)])]), r.createVNode("div", {
                    class: `${c}-modal-close`,
                    onClick: g => {
                        g.stopPropagation(), i.onClose && i.onClose()
                    }
                }, [r.createVNode("svg", {
                    class: `${c}-icon`,
                    "aria-hidden": "true"
                }, [r.createVNode("use", {"xlink:href": "#icon-close"}, null)])])])])])
            }
        }
    });
    const Ft = () => ({
        type: {type: String, default: "link"},
        visible: {type: Boolean, default: !1},
        onCancel: {
            type: Function, default: () => () => {
            }
        },
        onOk: {
            type: Function, default: () => () => {
            }
        }
    });
    var jt = r.defineComponent({
        props: Ft(), setup(i) {
            const t = r.inject("usedLanguageText"), e = r.inject("editorId"), n = r.computed(() => {
                var o, a, s, d;
                switch (i.type) {
                    case "link":
                        return `${(o = t.value.linkModalTips) == null ? void 0 : o.title}${(a = t.value.toolbarTips) == null ? void 0 : a.link}`;
                    case "image":
                        return `${(s = t.value.linkModalTips) == null ? void 0 : s.title}${(d = t.value.toolbarTips) == null ? void 0 : d.image}`;
                    default:
                        return ""
                }
            }), l = r.reactive({desc: "", url: ""});
            return r.watch(() => i.visible, o => {
                o || setTimeout(() => {
                    l.desc = "", l.url = ""
                }, 200)
            }), () => r.createVNode(je, {title: n.value, visible: i.visible, onClose: i.onCancel}, {
                default: () => {
                    var o, a, s, d, h;
                    return [r.createVNode("div", {class: `${c}-form-item`}, [r.createVNode("label", {
                        class: `${c}-lable`,
                        for: `link-desc-${e}`
                    }, [(o = t.value.linkModalTips) == null ? void 0 : o.descLable]), r.createVNode("input", {
                        placeholder: (a = t.value.linkModalTips) == null ? void 0 : a.descLablePlaceHolder,
                        class: `${c}-input`,
                        id: `link-desc-${e}`,
                        type: "text",
                        value: l.desc,
                        onChange: u => {
                            l.desc = u.target.value
                        },
                        autocomplete: "off"
                    }, null)]), r.createVNode("div", {class: `${c}-form-item`}, [r.createVNode("label", {
                        class: `${c}-lable`,
                        for: `link-url-${e}`
                    }, [(s = t.value.linkModalTips) == null ? void 0 : s.urlLable]), r.createVNode("input", {
                        placeholder: (d = t.value.linkModalTips) == null ? void 0 : d.UrlLablePlaceHolder,
                        class: `${c}-input`,
                        id: `link-url-${e}`,
                        type: "text",
                        value: l.url,
                        onChange: u => {
                            l.url = u.target.value
                        },
                        autocomplete: "off"
                    }, null)]), r.createVNode("div", {class: `${c}-form-item`}, [r.createVNode("button", {
                        class: [`${c}-btn`, `${c}-btn-row`],
                        type: "button",
                        onClick: () => {
                            i.onOk(l), l.desc = "", l.url = ""
                        }
                    }, [(h = t.value.linkModalTips) == null ? void 0 : h.buttonOK])])]
                }
            })
        }
    }), ei = "";
    const Dt = () => ({
        visible: {type: Boolean, default: !1}, onCancel: {
            type: Function, default: () => () => {
            }
        }, onOk: {
            type: Function, default: () => () => {
            }
        }
    });
    var _t = r.defineComponent({
        props: Dt(), setup(i) {
            var u, g;
            const t = r.inject("usedLanguageText"), e = r.inject("editorId");
            let n = (g = (u = M == null ? void 0 : M.editorExtensions) == null ? void 0 : u.cropper) == null ? void 0 : g.instance;
            const l = r.ref(), o = r.ref(), a = r.ref(),
                s = r.reactive({cropperInited: !1, imgSelected: !1, imgSrc: "", isFullscreen: !1});
            let d = null;
            r.watch(() => i.visible, () => {
                i.visible && !s.cropperInited && (n = n || window.Cropper, l.value.onchange = () => {
                    if (!n) {
                        C.emit(e, "errorCatcher", {name: "Cropper", message: "Cropper is undefined"});
                        return
                    }
                    const k = l.value.files || [];
                    if (s.imgSelected = !0, (k == null ? void 0 : k.length) > 0) {
                        const f = new FileReader;
                        f.onload = $ => {
                            s.imgSrc = $.target.result, r.nextTick(() => {
                                d = new n(o.value, {viewMode: 2, preview: `.${c}-clip-preview-target`})
                            })
                        }, f.readAsDataURL(k[0])
                    }
                })
            }), r.watch(() => [s.imgSelected], () => {
                a.value.style = ""
            }), r.watch(() => s.isFullscreen, () => {
                r.nextTick(() => {
                    d == null || d.destroy(), a.value.style = "", o.value && (d = new n(o.value, {
                        viewMode: 2,
                        preview: `.${c}-clip-preview-target`
                    }))
                })
            });
            const h = () => {
                d.destroy(), l.value.value = "", s.imgSelected = !1
            };
            return () => {
                var k;
                return r.createVNode(je, {
                    title: (k = t.value.clipModalTips) == null ? void 0 : k.title,
                    visible: i.visible,
                    onClose: i.onCancel,
                    showAdjust: !0,
                    isFullscreen: s.isFullscreen,
                    onAdjust: f => {
                        s.isFullscreen = f
                    },
                    width: "668px",
                    height: "421px"
                }, {
                    default: () => {
                        var f;
                        return [r.createVNode("div", {class: `${c}-form-item ${c}-clip`}, [r.createVNode("div", {class: `${c}-clip-main`}, [s.imgSelected ? r.createVNode("div", {class: `${c}-clip-cropper`}, [r.createVNode("img", {
                            src: s.imgSrc,
                            ref: o,
                            style: {display: "none"}
                        }, null), r.createVNode("div", {
                            class: `${c}-clip-delete`,
                            onClick: h
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-delete"}, null)])])]) : r.createVNode("div", {
                            class: `${c}-clip-upload`,
                            onClick: () => {
                                l.value.click()
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-upload"}, null)])])]), r.createVNode("div", {class: `${c}-clip-preview`}, [r.createVNode("div", {
                            class: `${c}-clip-preview-target`,
                            ref: a
                        }, null)])]), r.createVNode("div", {class: `${c}-form-item`}, [r.createVNode("button", {
                            class: `${c}-btn`,
                            type: "button",
                            onClick: () => {
                                const $ = d.getCroppedCanvas();
                                C.emit(e, "uploadImage", [$t($.toDataURL("image/png"))], i.onOk), h()
                            }
                        }, [(f = t.value.linkModalTips) == null ? void 0 : f.buttonOK])]), r.createVNode("input", {
                            ref: l,
                            accept: "image/*",
                            type: "file",
                            multiple: !1,
                            style: {display: "none"}
                        }, null)]
                    }
                })
            }
        }
    });
    const Rt = () => ({
        type: {type: String, default: "link"},
        linkVisible: {type: Boolean, default: !1},
        clipVisible: {type: Boolean, default: !1},
        onCancel: {
            type: Function, default: () => () => {
            }
        },
        onOk: {
            type: Function, default: () => () => {
            }
        }
    });
    var Pt = r.defineComponent({
        props: Rt(), setup(i) {
            return () => r.createVNode(r.Fragment, null, [r.createVNode(jt, {
                type: i.type,
                visible: i.linkVisible,
                onOk: i.onOk,
                onCancel: i.onCancel
            }, null), r.createVNode(_t, {visible: i.clipVisible, onOk: i.onOk, onCancel: i.onCancel}, null)])
        }
    });
    const Ht = i => {
        var h, u, g, k;
        const t = r.inject("editorId"), e = r.inject("previewOnly");
        let n = (u = (h = M.editorExtensions) == null ? void 0 : h.screenfull) == null ? void 0 : u.instance;
        const l = (k = (g = M.editorExtensions) == null ? void 0 : g.screenfull) == null ? void 0 : k.js, o = r.ref(!1),
            a = () => {
                if (!n) {
                    C.emit(t, "errorCatcher", {name: "fullScreen", message: "fullScreen is undefined"});
                    return
                }
                n.isEnabled ? (o.value = !0, n.isFullscreen ? n.exit() : n.request()) : console.error("browser does not support screenfull!")
            }, s = () => {
                n && n.isEnabled && n.on("change", () => {
                    (o.value || i.setting.fullscreen) && (o.value = !1, i.updateSetting(!i.setting.fullscreen, "fullscreen"))
                })
            }, d = () => {
                n = window.screenfull, s()
            };
        return r.onMounted(() => {
            if (s(), !e && !n) {
                const f = document.createElement("script");
                f.src = l || bt, f.onload = d, f.id = `${c}-screenfull`, G(f, "screenfull")
            }
        }), {fullScreenHandler: a}
    }, Ot = () => ({
        tableShape: {type: Array, default: () => [6, 4]}, onSelected: {
            type: Function, default: () => {
            }
        }
    }), Mt = r.defineComponent({
        name: "TableShape", props: Ot(), setup(i) {
            const t = r.reactive({x: -1, y: -1});
            return () => r.createVNode("div", {
                class: `${c}-table-shape`, onMouseleave: () => {
                    t.x = -1, t.y = -1
                }
            }, [new Array(i.tableShape[1]).fill("").map((e, n) => r.createVNode("div", {
                class: `${c}-table-shape-row`,
                key: `table-shape-row-${n}`
            }, [new Array(i.tableShape[0]).fill("").map((l, o) => r.createVNode("div", {
                class: `${c}-table-shape-col`,
                key: `table-shape-col-${o}`,
                onMouseenter: () => {
                    t.x = n, t.y = o
                },
                onClick: () => {
                    i.onSelected(t)
                }
            }, [r.createVNode("div", {class: [`${c}-table-shape-col-default`, n <= t.x && o <= t.y && `${c}-table-shape-col-include`]}, null)]))]))])
        }
    }), Bt = () => ({
        noPrettier: {type: Boolean},
        toolbars: {type: Array, default: () => []},
        toolbarsExclude: {type: Array, default: () => []},
        setting: {type: Object, default: () => ({})},
        screenfull: {type: Object, default: null},
        screenfullJs: {type: String, default: ""},
        updateSetting: {
            type: Function, default: () => () => {
            }
        },
        tableShape: {type: Array, default: () => [6, 4]},
        defToolbars: {type: Object}
    });
    var Ut = r.defineComponent({
        name: "MDEditorToolbar", props: Bt(), setup(i) {
            const t = r.inject("editorId"), e = r.inject("usedLanguageText"), {fullScreenHandler: n} = Ht(i),
                l = `${t}-toolbar-wrapper`,
                o = r.reactive({title: !1, catalog: !1, image: !1, table: !1, mermaid: !1, katex: !1}), a = (f, $) => {
                    C.emit(t, "replace", f, $)
                }, s = r.reactive({type: "link", linkVisible: !1, clipVisible: !1}), d = r.ref();
            r.onMounted(() => {
                C.on(t, {
                    name: "openModals", callback(f) {
                        s.type = f, s.linkVisible = !0
                    }
                })
            });
            const h = r.computed(() => {
                const f = i.toolbars.filter(b => !i.toolbarsExclude.includes(b)), $ = f.indexOf("="),
                    v = $ === -1 ? f : f.slice(0, $ + 1), w = $ === -1 ? [] : f.slice($, Number.MAX_SAFE_INTEGER);
                return [v, w]
            }), u = r.ref(), g = () => {
                C.emit(t, "uploadImage", Array.from(u.value.files || [])), u.value.value = ""
            };
            r.onMounted(() => {
                u.value.addEventListener("change", g)
            });
            const k = f => {
                var $, v, w, b, y, E, L, F, O, I, m, q, j, D, N, p, S, z, A, P, B, K, Z, ce, $e, Ce, Se, ie, Ee, Ne, re,
                    Te, Ve, fe, me, ze, Ae, de, Le, Ie, ge;
                if (Ue.includes(f)) switch (f) {
                    case "-":
                        return r.createVNode(At, null, null);
                    case "bold":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: ($ = e.value.toolbarTips) == null ? void 0 : $.bold,
                            onClick: () => {
                                a("bold")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-bold"}, null)])]);
                    case "underline":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (v = e.value.toolbarTips) == null ? void 0 : v.underline,
                            onClick: () => {
                                a("underline")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-underline"}, null)])]);
                    case "italic":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (w = e.value.toolbarTips) == null ? void 0 : w.italic,
                            onClick: () => {
                                a("italic")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-italic"}, null)])]);
                    case "strikeThrough":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (b = e.value.toolbarTips) == null ? void 0 : b.strikeThrough,
                            onClick: () => {
                                a("strikeThrough")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-strike-through"}, null)])]);
                    case "title":
                        return r.createVNode(oe, {
                            relative: `#${l}`, visible: o.title, onChange: _ => {
                                o.title = _
                            }, overlay: r.createVNode("ul", {
                                class: `${c}-menu`, onClick: () => {
                                    o.title = !1
                                }
                            }, [r.createVNode("li", {
                                class: `${c}-menu-item`, onClick: () => {
                                    a("h1")
                                }
                            }, [(y = e.value.titleItem) == null ? void 0 : y.h1]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("h2")
                                }
                            }, [(E = e.value.titleItem) == null ? void 0 : E.h2]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("h3")
                                }
                            }, [(L = e.value.titleItem) == null ? void 0 : L.h3]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("h4")
                                }
                            }, [(F = e.value.titleItem) == null ? void 0 : F.h4]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("h5")
                                }
                            }, [(O = e.value.titleItem) == null ? void 0 : O.h5]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("h6")
                                }
                            }, [(I = e.value.titleItem) == null ? void 0 : I.h6])])
                        }, {
                            default: () => {
                                var _;
                                return [r.createVNode("div", {
                                    class: `${c}-toolbar-item`,
                                    title: (_ = e.value.toolbarTips) == null ? void 0 : _.title
                                }, [r.createVNode("svg", {
                                    class: `${c}-icon`,
                                    "aria-hidden": "true"
                                }, [r.createVNode("use", {"xlink:href": "#icon-title"}, null)])])]
                            }
                        });
                    case "sub":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (m = e.value.toolbarTips) == null ? void 0 : m.sub,
                            onClick: () => {
                                a("sub")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-sub"}, null)])]);
                    case "sup":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (q = e.value.toolbarTips) == null ? void 0 : q.sup,
                            onClick: () => {
                                a("sup")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-sup"}, null)])]);
                    case "quote":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (j = e.value.toolbarTips) == null ? void 0 : j.quote,
                            onClick: () => {
                                a("quote")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-quote"}, null)])]);
                    case "unorderedList":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (D = e.value.toolbarTips) == null ? void 0 : D.unorderedList,
                            onClick: () => {
                                a("unorderedList")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-unordered-list"}, null)])]);
                    case "orderedList":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (N = e.value.toolbarTips) == null ? void 0 : N.orderedList,
                            onClick: () => {
                                a("orderedList")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-ordered-list"}, null)])]);
                    case "codeRow":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (p = e.value.toolbarTips) == null ? void 0 : p.codeRow,
                            onClick: () => {
                                a("codeRow")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-code-row"}, null)])]);
                    case "code":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (S = e.value.toolbarTips) == null ? void 0 : S.code,
                            onClick: () => {
                                a("code")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-code"}, null)])]);
                    case "link":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (z = e.value.toolbarTips) == null ? void 0 : z.link,
                            onClick: () => {
                                s.type = "link", s.linkVisible = !0
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-link"}, null)])]);
                    case "image":
                        return r.createVNode(oe, {
                            relative: `#${l}`, visible: o.image, onChange: _ => {
                                o.image = _
                            }, overlay: r.createVNode("ul", {
                                class: `${c}-menu`, onClick: () => {
                                    o.title = !1
                                }
                            }, [r.createVNode("li", {
                                class: `${c}-menu-item`, onClick: () => {
                                    s.type = "image", s.linkVisible = !0
                                }
                            }, [(A = e.value.imgTitleItem) == null ? void 0 : A.link]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    u.value.click()
                                }
                            }, [(P = e.value.imgTitleItem) == null ? void 0 : P.upload]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    s.clipVisible = !0
                                }
                            }, [(B = e.value.imgTitleItem) == null ? void 0 : B.clip2upload])])
                        }, {
                            default: () => {
                                var _;
                                return [r.createVNode("div", {
                                    class: `${c}-toolbar-item`,
                                    title: (_ = e.value.toolbarTips) == null ? void 0 : _.image
                                }, [r.createVNode("svg", {
                                    class: `${c}-icon`,
                                    "aria-hidden": "true"
                                }, [r.createVNode("use", {"xlink:href": "#icon-image"}, null)])])]
                            }
                        });
                    case "table":
                        return r.createVNode(oe, {
                            relative: `#${l}`,
                            visible: o.table,
                            onChange: _ => {
                                o.table = _
                            },
                            key: "bar-table",
                            overlay: r.createVNode(Mt, {
                                tableShape: i.tableShape, onSelected: _ => {
                                    a("table", {selectedShape: _})
                                }
                            }, null)
                        }, {
                            default: () => {
                                var _;
                                return [r.createVNode("div", {
                                    class: `${c}-toolbar-item`,
                                    title: (_ = e.value.toolbarTips) == null ? void 0 : _.table
                                }, [r.createVNode("svg", {
                                    class: `${c}-icon`,
                                    "aria-hidden": "true"
                                }, [r.createVNode("use", {"xlink:href": "#icon-table"}, null)])])]
                            }
                        });
                    case "revoke":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (K = e.value.toolbarTips) == null ? void 0 : K.revoke,
                            onClick: () => {
                                C.emit(t, "ctrlZ")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-revoke"}, null)])]);
                    case "next":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (Z = e.value.toolbarTips) == null ? void 0 : Z.next,
                            onClick: () => {
                                C.emit(t, "ctrlShiftZ")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-next"}, null)])]);
                    case "save":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (ce = e.value.toolbarTips) == null ? void 0 : ce.save,
                            onClick: () => {
                                C.emit(t, "onSave")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-baocun"}, null)])]);
                    case "prettier":
                        return i.noPrettier ? "" : r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: ($e = e.value.toolbarTips) == null ? void 0 : $e.prettier,
                            onClick: () => {
                                a("prettier")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-prettier"}, null)])]);
                    case "pageFullscreen":
                        return !i.setting.fullscreen && r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (Ce = e.value.toolbarTips) == null ? void 0 : Ce.pageFullscreen,
                            onClick: () => {
                                i.updateSetting(!i.setting.pageFullScreen, "pageFullScreen")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": `#icon-${i.setting.pageFullScreen ? "suoxiao" : "fangda"}`}, null)])]);
                    case "fullscreen":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (Se = e.value.toolbarTips) == null ? void 0 : Se.fullscreen,
                            onClick: n
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": `#icon-${i.setting.fullscreen ? "fullScreen-exit" : "fullScreen"}`}, null)])]);
                    case "preview":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (ie = e.value.toolbarTips) == null ? void 0 : ie.preview,
                            onClick: () => {
                                i.updateSetting(!i.setting.preview, "preview")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-preview"}, null)])]);
                    case "htmlPreview":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (Ee = e.value.toolbarTips) == null ? void 0 : Ee.htmlPreview,
                            onClick: () => {
                                i.updateSetting(!i.setting.htmlPreview, "htmlPreview")
                            }
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-coding"}, null)])]);
                    case "catalog":
                        return r.createVNode("div", {
                            class: `${c}-toolbar-item`,
                            title: (Ne = e.value.toolbarTips) == null ? void 0 : Ne.catalog,
                            onClick: () => {
                                C.emit(t, "catalogShow")
                            },
                            key: "bar-catalog"
                        }, [r.createVNode("svg", {
                            class: `${c}-icon`,
                            "aria-hidden": "true"
                        }, [r.createVNode("use", {"xlink:href": "#icon-catalog"}, null)])]);
                    // case "github":
                    //     return r.createVNode("div", {
                    //         class: `${c}-toolbar-item`,
                    //         title: (re = e.value.toolbarTips) == null ? void 0 : re.github,
                    //         onClick: () => xt("https://github.com/imzbf/md-editor-v3")
                    //     }, [r.createVNode("svg", {
                    //         class: `${c}-icon`,
                    //         "aria-hidden": "true"
                    //     }, [r.createVNode("use", {"xlink:href": "#icon-github"}, null)])]);
                    case "mermaid":
                        return r.createVNode(oe, {
                            relative: `#${l}`, visible: o.mermaid, onChange: _ => {
                                o.mermaid = _
                            }, overlay: r.createVNode("ul", {
                                class: `${c}-menu`, onClick: () => {
                                    o.mermaid = !1
                                }
                            }, [r.createVNode("li", {
                                class: `${c}-menu-item`, onClick: () => {
                                    a("flow")
                                }
                            }, [(Te = e.value.mermaid) == null ? void 0 : Te.flow]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("sequence")
                                }
                            }, [(Ve = e.value.mermaid) == null ? void 0 : Ve.sequence]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("gantt")
                                }
                            }, [(fe = e.value.mermaid) == null ? void 0 : fe.gantt]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("class")
                                }
                            }, [(me = e.value.mermaid) == null ? void 0 : me.class]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("state")
                                }
                            }, [(ze = e.value.mermaid) == null ? void 0 : ze.state]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("pie")
                                }
                            }, [(Ae = e.value.mermaid) == null ? void 0 : Ae.pie]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("relationship")
                                }
                            }, [(de = e.value.mermaid) == null ? void 0 : de.relationship]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("journey")
                                }
                            }, [(Le = e.value.mermaid) == null ? void 0 : Le.journey])]), key: "bar-mermaid"
                        }, {
                            default: () => {
                                var _;
                                return [r.createVNode("div", {
                                    class: `${c}-toolbar-item`,
                                    title: (_ = e.value.toolbarTips) == null ? void 0 : _.mermaid
                                }, [r.createVNode("svg", {
                                    class: `${c}-icon`,
                                    "aria-hidden": "true"
                                }, [r.createVNode("use", {"xlink:href": "#icon-mermaid"}, null)])])]
                            }
                        });
                    case "katex":
                        return r.createVNode(oe, {
                            relative: `#${l}`, visible: o.katex, onChange: _ => {
                                o.katex = _
                            }, overlay: r.createVNode("ul", {
                                class: `${c}-menu`, onClick: () => {
                                    o.katex = !1
                                }
                            }, [r.createVNode("li", {
                                class: `${c}-menu-item`, onClick: () => {
                                    a("katexInline")
                                }
                            }, [(Ie = e.value.katex) == null ? void 0 : Ie.inline]), r.createVNode("li", {
                                class: `${c}-menu-item`,
                                onClick: () => {
                                    a("katexBlock")
                                }
                            }, [(ge = e.value.katex) == null ? void 0 : ge.block])]), key: "bar-katex"
                        }, {
                            default: () => {
                                var _;
                                return [r.createVNode("div", {
                                    class: `${c}-toolbar-item`,
                                    title: (_ = e.value.toolbarTips) == null ? void 0 : _.katex
                                }, [r.createVNode("svg", {
                                    class: `${c}-icon`,
                                    "aria-hidden": "true"
                                }, [r.createVNode("use", {"xlink:href": "#icon-formula"}, null)])])]
                            }
                        })
                } else return i.defToolbars instanceof Array ? i.defToolbars[f] || "" : i.defToolbars && i.defToolbars.children instanceof Array && i.defToolbars.children[f] || ""
            };
            return () => {
                const f = h.value[0].map(v => k(v)), $ = h.value[1].map(v => k(v));
                return r.createVNode("div", {
                    class: `${c}-toolbar-wrapper`,
                    id: l
                }, [r.createVNode("div", {
                    class: `${c}-toolbar`, onMouseenter: () => {
                        C.emit(t, "selectTextChange")
                    }
                }, [r.createVNode("div", {
                    class: `${c}-toolbar-left`,
                    ref: d
                }, [f]), r.createVNode("div", {class: `${c}-toolbar-right`}, [$])]), r.createVNode("input", {
                    ref: u,
                    accept: "image/*",
                    type: "file",
                    multiple: !0,
                    style: {display: "none"}
                }, null), r.createVNode(Pt, {
                    linkVisible: s.linkVisible,
                    clipVisible: s.clipVisible,
                    type: s.type,
                    onCancel: () => {
                        s.linkVisible = !1, s.clipVisible = !1
                    },
                    onOk: v => {
                        v && a(s.type, {desc: v.desc, url: v.url}), s.linkVisible = !1, s.clipVisible = !1
                    }
                }, null)])
            }
        }
    });

    function Je() {
        return {
            baseUrl: null,
            breaks: !1,
            extensions: null,
            gfm: !0,
            headerIds: !0,
            headerPrefix: "",
            highlight: null,
            langPrefix: "language-",
            mangle: !0,
            pedantic: !1,
            renderer: null,
            sanitize: !1,
            sanitizer: null,
            silent: !1,
            smartLists: !1,
            smartypants: !1,
            tokenizer: null,
            walkTokens: null,
            xhtml: !1
        }
    }

    let ae = Je();

    function qt(i) {
        ae = i
    }

    const Kt = /[&<>"']/, Zt = /[&<>"']/g, Wt = /[<>"']|&(?!#?\w+;)/, Gt = /[<>"']|&(?!#?\w+;)/g,
        Qt = {"&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"}, et = i => Qt[i];

    function U(i, t) {
        if (t) {
            if (Kt.test(i)) return i.replace(Zt, et)
        } else if (Wt.test(i)) return i.replace(Gt, et);
        return i
    }

    const Xt = /&(#(?:\d+)|(?:#x[0-9A-Fa-f]+)|(?:\w+));?/ig;

    function tt(i) {
        return i.replace(Xt, (t, e) => (e = e.toLowerCase(), e === "colon" ? ":" : e.charAt(0) === "#" ? e.charAt(1) === "x" ? String.fromCharCode(parseInt(e.substring(2), 16)) : String.fromCharCode(+e.substring(1)) : ""))
    }

    const Yt = /(^|[^\[])\^/g;

    function R(i, t) {
        i = typeof i == "string" ? i : i.source, t = t || "";
        const e = {
            replace: (n, l) => (l = l.source || l, l = l.replace(Yt, "$1"), i = i.replace(n, l), e),
            getRegex: () => new RegExp(i, t)
        };
        return e
    }

    const Jt = /[^\w:]/g, en = /^$|^[a-z][a-z0-9+.-]*:|^[?#]/i;

    function nt(i, t, e) {
        if (i) {
            let n;
            try {
                n = decodeURIComponent(tt(e)).replace(Jt, "").toLowerCase()
            } catch {
                return null
            }
            if (n.indexOf("javascript:") === 0 || n.indexOf("vbscript:") === 0 || n.indexOf("data:") === 0) return null
        }
        t && !en.test(e) && (e = rn(t, e));
        try {
            e = encodeURI(e).replace(/%25/g, "%")
        } catch {
            return null
        }
        return e
    }

    const ke = {}, tn = /^[^:]+:\/*[^/]*$/, nn = /^([^:]+:)[\s\S]*$/, ln = /^([^:]+:\/*[^/]*)[\s\S]*$/;

    function rn(i, t) {
        ke[" " + i] || (tn.test(i) ? ke[" " + i] = i + "/" : ke[" " + i] = ye(i, "/", !0)), i = ke[" " + i];
        const e = i.indexOf(":") === -1;
        return t.substring(0, 2) === "//" ? e ? t : i.replace(nn, "$1") + t : t.charAt(0) === "/" ? e ? t : i.replace(ln, "$1") + t : i + t
    }

    const be = {
        exec: function () {
        }
    };

    function X(i) {
        let t = 1, e, n;
        for (; t < arguments.length; t++) {
            e = arguments[t];
            for (n in e) Object.prototype.hasOwnProperty.call(e, n) && (i[n] = e[n])
        }
        return i
    }

    function it(i, t) {
        const e = i.replace(/\|/g, (o, a, s) => {
            let d = !1, h = a;
            for (; --h >= 0 && s[h] === "\\";) d = !d;
            return d ? "|" : " |"
        }), n = e.split(/ \|/);
        let l = 0;
        if (n[0].trim() || n.shift(), n.length > 0 && !n[n.length - 1].trim() && n.pop(), n.length > t) n.splice(t); else for (; n.length < t;) n.push("");
        for (; l < n.length; l++) n[l] = n[l].trim().replace(/\\\|/g, "|");
        return n
    }

    function ye(i, t, e) {
        const n = i.length;
        if (n === 0) return "";
        let l = 0;
        for (; l < n;) {
            const o = i.charAt(n - l - 1);
            if (o === t && !e) l++; else if (o !== t && e) l++; else break
        }
        return i.slice(0, n - l)
    }

    function on(i, t) {
        if (i.indexOf(t[1]) === -1) return -1;
        const e = i.length;
        let n = 0, l = 0;
        for (; l < e; l++) if (i[l] === "\\") l++; else if (i[l] === t[0]) n++; else if (i[l] === t[1] && (n--, n < 0)) return l;
        return -1
    }

    function lt(i) {
        i && i.sanitize && !i.silent && console.warn("marked(): sanitize and sanitizer parameters are deprecated since version 0.7.0, should not be used and will be removed in the future. Read more here: https://marked.js.org/#/USING_ADVANCED.md#options")
    }

    function rt(i, t) {
        if (t < 1) return "";
        let e = "";
        for (; t > 1;) t & 1 && (e += i), t >>= 1, i += i;
        return e + i
    }

    function ot(i, t, e, n) {
        const l = t.href, o = t.title ? U(t.title) : null, a = i[1].replace(/\\([\[\]])/g, "$1");
        if (i[0].charAt(0) !== "!") {
            n.state.inLink = !0;
            const s = {type: "link", raw: e, href: l, title: o, text: a, tokens: n.inlineTokens(a, [])};
            return n.state.inLink = !1, s
        }
        return {type: "image", raw: e, href: l, title: o, text: U(a)}
    }

    function an(i, t) {
        const e = i.match(/^(\s+)(?:```)/);
        if (e === null) return t;
        const n = e[1];
        return t.split(`
`).map(l => {
            const o = l.match(/^\s+/);
            if (o === null) return l;
            const [a] = o;
            return a.length >= n.length ? l.slice(n.length) : l
        }).join(`
`)
    }

    class De {
        constructor(t) {
            this.options = t || ae
        }

        space(t) {
            const e = this.rules.block.newline.exec(t);
            if (e && e[0].length > 0) return {type: "space", raw: e[0]}
        }

        code(t) {
            const e = this.rules.block.code.exec(t);
            if (e) {
                const n = e[0].replace(/^ {1,4}/gm, "");
                return {
                    type: "code", raw: e[0], codeBlockStyle: "indented", text: this.options.pedantic ? n : ye(n, `
`)
                }
            }
        }

        fences(t) {
            const e = this.rules.block.fences.exec(t);
            if (e) {
                const n = e[0], l = an(n, e[3] || "");
                return {type: "code", raw: n, lang: e[2] ? e[2].trim() : e[2], text: l}
            }
        }

        heading(t) {
            const e = this.rules.block.heading.exec(t);
            if (e) {
                let n = e[2].trim();
                if (/#$/.test(n)) {
                    const o = ye(n, "#");
                    (this.options.pedantic || !o || / $/.test(o)) && (n = o.trim())
                }
                const l = {type: "heading", raw: e[0], depth: e[1].length, text: n, tokens: []};
                return this.lexer.inline(l.text, l.tokens), l
            }
        }

        hr(t) {
            const e = this.rules.block.hr.exec(t);
            if (e) return {type: "hr", raw: e[0]}
        }

        blockquote(t) {
            const e = this.rules.block.blockquote.exec(t);
            if (e) {
                const n = e[0].replace(/^ *>[ \t]?/gm, "");
                return {type: "blockquote", raw: e[0], tokens: this.lexer.blockTokens(n, []), text: n}
            }
        }

        list(t) {
            let e = this.rules.block.list.exec(t);
            if (e) {
                let n, l, o, a, s, d, h, u, g, k, f, $, v = e[1].trim();
                const w = v.length > 1,
                    b = {type: "list", raw: "", ordered: w, start: w ? +v.slice(0, -1) : "", loose: !1, items: []};
                v = w ? `\\d{1,9}\\${v.slice(-1)}` : `\\${v}`, this.options.pedantic && (v = w ? v : "[*+-]");
                const y = new RegExp(`^( {0,3}${v})((?:[	 ][^\\n]*)?(?:\\n|$))`);
                for (; t && ($ = !1, !(!(e = y.exec(t)) || this.rules.block.hr.test(t)));) {
                    if (n = e[0], t = t.substring(n.length), u = e[2].split(`
`, 1)[0], g = t.split(`
`, 1)[0], this.options.pedantic ? (a = 2, f = u.trimLeft()) : (a = e[2].search(/[^ ]/), a = a > 4 ? 1 : a, f = u.slice(a), a += e[1].length), d = !1, !u && /^ *$/.test(g) && (n += g + `
`, t = t.substring(g.length + 1), $ = !0), !$) {
                        const L = new RegExp(`^ {0,${Math.min(3, a - 1)}}(?:[*+-]|\\d{1,9}[.)])((?: [^\\n]*)?(?:\\n|$))`),
                            F = new RegExp(`^ {0,${Math.min(3, a - 1)}}((?:- *){3,}|(?:_ *){3,}|(?:\\* *){3,})(?:\\n+|$)`);
                        for (; t && (k = t.split(`
`, 1)[0], u = k, this.options.pedantic && (u = u.replace(/^ {1,4}(?=( {4})*[^ ])/g, "  ")), !(L.test(u) || F.test(t)));) {
                            if (u.search(/[^ ]/) >= a || !u.trim()) f += `
` + u.slice(a); else if (!d) f += `
` + u; else break;
                            !d && !u.trim() && (d = !0), n += k + `
`, t = t.substring(k.length + 1)
                        }
                    }
                    b.loose || (h ? b.loose = !0 : /\n *\n *$/.test(n) && (h = !0)), this.options.gfm && (l = /^\[[ xX]\] /.exec(f), l && (o = l[0] !== "[ ] ", f = f.replace(/^\[[ xX]\] +/, ""))), b.items.push({
                        type: "list_item",
                        raw: n,
                        task: !!l,
                        checked: o,
                        loose: !1,
                        text: f
                    }), b.raw += n
                }
                b.items[b.items.length - 1].raw = n.trimRight(), b.items[b.items.length - 1].text = f.trimRight(), b.raw = b.raw.trimRight();
                const E = b.items.length;
                for (s = 0; s < E; s++) {
                    this.lexer.state.top = !1, b.items[s].tokens = this.lexer.blockTokens(b.items[s].text, []);
                    const L = b.items[s].tokens.filter(O => O.type === "space"), F = L.every(O => {
                        const I = O.raw.split("");
                        let m = 0;
                        for (const q of I) if (q === `
` && (m += 1), m > 1) return !0;
                        return !1
                    });
                    !b.loose && L.length && F && (b.loose = !0, b.items[s].loose = !0)
                }
                return b
            }
        }

        html(t) {
            const e = this.rules.block.html.exec(t);
            if (e) {
                const n = {
                    type: "html",
                    raw: e[0],
                    pre: !this.options.sanitizer && (e[1] === "pre" || e[1] === "script" || e[1] === "style"),
                    text: e[0]
                };
                return this.options.sanitize && (n.type = "paragraph", n.text = this.options.sanitizer ? this.options.sanitizer(e[0]) : U(e[0]), n.tokens = [], this.lexer.inline(n.text, n.tokens)), n
            }
        }

        def(t) {
            const e = this.rules.block.def.exec(t);
            if (e) {
                e[3] && (e[3] = e[3].substring(1, e[3].length - 1));
                const n = e[1].toLowerCase().replace(/\s+/g, " ");
                return {type: "def", tag: n, raw: e[0], href: e[2], title: e[3]}
            }
        }

        table(t) {
            const e = this.rules.block.table.exec(t);
            if (e) {
                const n = {
                    type: "table",
                    header: it(e[1]).map(l => ({text: l})),
                    align: e[2].replace(/^ *|\| *$/g, "").split(/ *\| */),
                    rows: e[3] && e[3].trim() ? e[3].replace(/\n[ \t]*$/, "").split(`
`) : []
                };
                if (n.header.length === n.align.length) {
                    n.raw = e[0];
                    let l = n.align.length, o, a, s, d;
                    for (o = 0; o < l; o++) /^ *-+: *$/.test(n.align[o]) ? n.align[o] = "right" : /^ *:-+: *$/.test(n.align[o]) ? n.align[o] = "center" : /^ *:-+ *$/.test(n.align[o]) ? n.align[o] = "left" : n.align[o] = null;
                    for (l = n.rows.length, o = 0; o < l; o++) n.rows[o] = it(n.rows[o], n.header.length).map(h => ({text: h}));
                    for (l = n.header.length, a = 0; a < l; a++) n.header[a].tokens = [], this.lexer.inlineTokens(n.header[a].text, n.header[a].tokens);
                    for (l = n.rows.length, a = 0; a < l; a++) for (d = n.rows[a], s = 0; s < d.length; s++) d[s].tokens = [], this.lexer.inlineTokens(d[s].text, d[s].tokens);
                    return n
                }
            }
        }

        lheading(t) {
            const e = this.rules.block.lheading.exec(t);
            if (e) {
                const n = {type: "heading", raw: e[0], depth: e[2].charAt(0) === "=" ? 1 : 2, text: e[1], tokens: []};
                return this.lexer.inline(n.text, n.tokens), n
            }
        }

        paragraph(t) {
            const e = this.rules.block.paragraph.exec(t);
            if (e) {
                const n = {
                    type: "paragraph", raw: e[0], text: e[1].charAt(e[1].length - 1) === `
` ? e[1].slice(0, -1) : e[1], tokens: []
                };
                return this.lexer.inline(n.text, n.tokens), n
            }
        }

        text(t) {
            const e = this.rules.block.text.exec(t);
            if (e) {
                const n = {type: "text", raw: e[0], text: e[0], tokens: []};
                return this.lexer.inline(n.text, n.tokens), n
            }
        }

        escape(t) {
            const e = this.rules.inline.escape.exec(t);
            if (e) return {type: "escape", raw: e[0], text: U(e[1])}
        }

        tag(t) {
            const e = this.rules.inline.tag.exec(t);
            if (e) return !this.lexer.state.inLink && /^<a /i.test(e[0]) ? this.lexer.state.inLink = !0 : this.lexer.state.inLink && /^<\/a>/i.test(e[0]) && (this.lexer.state.inLink = !1), !this.lexer.state.inRawBlock && /^<(pre|code|kbd|script)(\s|>)/i.test(e[0]) ? this.lexer.state.inRawBlock = !0 : this.lexer.state.inRawBlock && /^<\/(pre|code|kbd|script)(\s|>)/i.test(e[0]) && (this.lexer.state.inRawBlock = !1), {
                type: this.options.sanitize ? "text" : "html",
                raw: e[0],
                inLink: this.lexer.state.inLink,
                inRawBlock: this.lexer.state.inRawBlock,
                text: this.options.sanitize ? this.options.sanitizer ? this.options.sanitizer(e[0]) : U(e[0]) : e[0]
            }
        }

        link(t) {
            const e = this.rules.inline.link.exec(t);
            if (e) {
                const n = e[2].trim();
                if (!this.options.pedantic && /^</.test(n)) {
                    if (!/>$/.test(n)) return;
                    const a = ye(n.slice(0, -1), "\\");
                    if ((n.length - a.length) % 2 === 0) return
                } else {
                    const a = on(e[2], "()");
                    if (a > -1) {
                        const d = (e[0].indexOf("!") === 0 ? 5 : 4) + e[1].length + a;
                        e[2] = e[2].substring(0, a), e[0] = e[0].substring(0, d).trim(), e[3] = ""
                    }
                }
                let l = e[2], o = "";
                if (this.options.pedantic) {
                    const a = /^([^'"]*[^\s])\s+(['"])(.*)\2/.exec(l);
                    a && (l = a[1], o = a[3])
                } else o = e[3] ? e[3].slice(1, -1) : "";
                return l = l.trim(), /^</.test(l) && (this.options.pedantic && !/>$/.test(n) ? l = l.slice(1) : l = l.slice(1, -1)), ot(e, {
                    href: l && l.replace(this.rules.inline._escapes, "$1"),
                    title: o && o.replace(this.rules.inline._escapes, "$1")
                }, e[0], this.lexer)
            }
        }

        reflink(t, e) {
            let n;
            if ((n = this.rules.inline.reflink.exec(t)) || (n = this.rules.inline.nolink.exec(t))) {
                let l = (n[2] || n[1]).replace(/\s+/g, " ");
                if (l = e[l.toLowerCase()], !l || !l.href) {
                    const o = n[0].charAt(0);
                    return {type: "text", raw: o, text: o}
                }
                return ot(n, l, n[0], this.lexer)
            }
        }

        emStrong(t, e, n = "") {
            let l = this.rules.inline.emStrong.lDelim.exec(t);
            if (!l || l[3] && n.match(/[\p{L}\p{N}]/u)) return;
            const o = l[1] || l[2] || "";
            if (!o || o && (n === "" || this.rules.inline.punctuation.exec(n))) {
                const a = l[0].length - 1;
                let s, d, h = a, u = 0;
                const g = l[0][0] === "*" ? this.rules.inline.emStrong.rDelimAst : this.rules.inline.emStrong.rDelimUnd;
                for (g.lastIndex = 0, e = e.slice(-1 * t.length + a); (l = g.exec(e)) != null;) {
                    if (s = l[1] || l[2] || l[3] || l[4] || l[5] || l[6], !s) continue;
                    if (d = s.length, l[3] || l[4]) {
                        h += d;
                        continue
                    } else if ((l[5] || l[6]) && a % 3 && !((a + d) % 3)) {
                        u += d;
                        continue
                    }
                    if (h -= d, h > 0) continue;
                    if (d = Math.min(d, d + h + u), Math.min(a, d) % 2) {
                        const f = t.slice(1, a + l.index + d);
                        return {
                            type: "em",
                            raw: t.slice(0, a + l.index + d + 1),
                            text: f,
                            tokens: this.lexer.inlineTokens(f, [])
                        }
                    }
                    const k = t.slice(2, a + l.index + d - 1);
                    return {
                        type: "strong",
                        raw: t.slice(0, a + l.index + d + 1),
                        text: k,
                        tokens: this.lexer.inlineTokens(k, [])
                    }
                }
            }
        }

        codespan(t) {
            const e = this.rules.inline.code.exec(t);
            if (e) {
                let n = e[2].replace(/\n/g, " ");
                const l = /[^ ]/.test(n), o = /^ /.test(n) && / $/.test(n);
                return l && o && (n = n.substring(1, n.length - 1)), n = U(n, !0), {
                    type: "codespan",
                    raw: e[0],
                    text: n
                }
            }
        }

        br(t) {
            const e = this.rules.inline.br.exec(t);
            if (e) return {type: "br", raw: e[0]}
        }

        del(t) {
            const e = this.rules.inline.del.exec(t);
            if (e) return {type: "del", raw: e[0], text: e[2], tokens: this.lexer.inlineTokens(e[2], [])}
        }

        autolink(t, e) {
            const n = this.rules.inline.autolink.exec(t);
            if (n) {
                let l, o;
                return n[2] === "@" ? (l = U(this.options.mangle ? e(n[1]) : n[1]), o = "mailto:" + l) : (l = U(n[1]), o = l), {
                    type: "link",
                    raw: n[0],
                    text: l,
                    href: o,
                    tokens: [{type: "text", raw: l, text: l}]
                }
            }
        }

        url(t, e) {
            let n;
            if (n = this.rules.inline.url.exec(t)) {
                let l, o;
                if (n[2] === "@") l = U(this.options.mangle ? e(n[0]) : n[0]), o = "mailto:" + l; else {
                    let a;
                    do a = n[0], n[0] = this.rules.inline._backpedal.exec(n[0])[0]; while (a !== n[0]);
                    l = U(n[0]), n[1] === "www." ? o = "http://" + l : o = l
                }
                return {type: "link", raw: n[0], text: l, href: o, tokens: [{type: "text", raw: l, text: l}]}
            }
        }

        inlineText(t, e) {
            const n = this.rules.inline.text.exec(t);
            if (n) {
                let l;
                return this.lexer.state.inRawBlock ? l = this.options.sanitize ? this.options.sanitizer ? this.options.sanitizer(n[0]) : U(n[0]) : n[0] : l = U(this.options.smartypants ? e(n[0]) : n[0]), {
                    type: "text",
                    raw: n[0],
                    text: l
                }
            }
        }
    }

    const T = {
        newline: /^(?: *(?:\n|$))+/,
        code: /^( {4}[^\n]+(?:\n(?: *(?:\n|$))*)?)+/,
        fences: /^ {0,3}(`{3,}(?=[^`\n]*\n)|~{3,})([^\n]*)\n(?:|([\s\S]*?)\n)(?: {0,3}\1[~`]* *(?=\n|$)|$)/,
        hr: /^ {0,3}((?:-[\t ]*){3,}|(?:_[ \t]*){3,}|(?:\*[ \t]*){3,})(?:\n+|$)/,
        heading: /^ {0,3}(#{1,6})(?=\s|$)(.*)(?:\n+|$)/,
        blockquote: /^( {0,3}> ?(paragraph|[^\n]*)(?:\n|$))+/,
        list: /^( {0,3}bull)([ \t][^\n]+?)?(?:\n|$)/,
        html: "^ {0,3}(?:<(script|pre|style|textarea)[\\s>][\\s\\S]*?(?:</\\1>[^\\n]*\\n+|$)|comment[^\\n]*(\\n+|$)|<\\?[\\s\\S]*?(?:\\?>\\n*|$)|<![A-Z][\\s\\S]*?(?:>\\n*|$)|<!\\[CDATA\\[[\\s\\S]*?(?:\\]\\]>\\n*|$)|</?(tag)(?: +|\\n|/?>)[\\s\\S]*?(?:(?:\\n *)+\\n|$)|<(?!script|pre|style|textarea)([a-z][\\w-]*)(?:attribute)*? */?>(?=[ \\t]*(?:\\n|$))[\\s\\S]*?(?:(?:\\n *)+\\n|$)|</(?!script|pre|style|textarea)[a-z][\\w-]*\\s*>(?=[ \\t]*(?:\\n|$))[\\s\\S]*?(?:(?:\\n *)+\\n|$))",
        def: /^ {0,3}\[(label)\]: *(?:\n *)?<?([^\s>]+)>?(?:(?: +(?:\n *)?| *\n *)(title))? *(?:\n+|$)/,
        table: be,
        lheading: /^([^\n]+)\n {0,3}(=+|-+) *(?:\n+|$)/,
        _paragraph: /^([^\n]+(?:\n(?!hr|heading|lheading|blockquote|fences|list|html|table| +\n)[^\n]+)*)/,
        text: /^[^\n]+/
    };
    T._label = /(?!\s*\])(?:\\.|[^\[\]\\])+/, T._title = /(?:"(?:\\"?|[^"\\])*"|'[^'\n]*(?:\n[^'\n]+)*\n?'|\([^()]*\))/, T.def = R(T.def).replace("label", T._label).replace("title", T._title).getRegex(), T.bullet = /(?:[*+-]|\d{1,9}[.)])/, T.listItemStart = R(/^( *)(bull) */).replace("bull", T.bullet).getRegex(), T.list = R(T.list).replace(/bull/g, T.bullet).replace("hr", "\\n+(?=\\1?(?:(?:- *){3,}|(?:_ *){3,}|(?:\\* *){3,})(?:\\n+|$))").replace("def", "\\n+(?=" + T.def.source + ")").getRegex(), T._tag = "address|article|aside|base|basefont|blockquote|body|caption|center|col|colgroup|dd|details|dialog|dir|div|dl|dt|fieldset|figcaption|figure|footer|form|frame|frameset|h[1-6]|head|header|hr|html|iframe|legend|li|link|main|menu|menuitem|meta|nav|noframes|ol|optgroup|option|p|param|section|source|summary|table|tbody|td|tfoot|th|thead|title|tr|track|ul", T._comment = /<!--(?!-?>)[\s\S]*?(?:-->|$)/, T.html = R(T.html, "i").replace("comment", T._comment).replace("tag", T._tag).replace("attribute", / +[a-zA-Z:_][\w.:-]*(?: *= *"[^"\n]*"| *= *'[^'\n]*'| *= *[^\s"'=<>`]+)?/).getRegex(), T.paragraph = R(T._paragraph).replace("hr", T.hr).replace("heading", " {0,3}#{1,6} ").replace("|lheading", "").replace("|table", "").replace("blockquote", " {0,3}>").replace("fences", " {0,3}(?:`{3,}(?=[^`\\n]*\\n)|~{3,})[^\\n]*\\n").replace("list", " {0,3}(?:[*+-]|1[.)]) ").replace("html", "</?(?:tag)(?: +|\\n|/?>)|<(?:script|pre|style|textarea|!--)").replace("tag", T._tag).getRegex(), T.blockquote = R(T.blockquote).replace("paragraph", T.paragraph).getRegex(), T.normal = X({}, T), T.gfm = X({}, T.normal, {table: "^ *([^\\n ].*\\|.*)\\n {0,3}(?:\\| *)?(:?-+:? *(?:\\| *:?-+:? *)*)(?:\\| *)?(?:\\n((?:(?! *\\n|hr|heading|blockquote|code|fences|list|html).*(?:\\n|$))*)\\n*|$)"}), T.gfm.table = R(T.gfm.table).replace("hr", T.hr).replace("heading", " {0,3}#{1,6} ").replace("blockquote", " {0,3}>").replace("code", " {4}[^\\n]").replace("fences", " {0,3}(?:`{3,}(?=[^`\\n]*\\n)|~{3,})[^\\n]*\\n").replace("list", " {0,3}(?:[*+-]|1[.)]) ").replace("html", "</?(?:tag)(?: +|\\n|/?>)|<(?:script|pre|style|textarea|!--)").replace("tag", T._tag).getRegex(), T.gfm.paragraph = R(T._paragraph).replace("hr", T.hr).replace("heading", " {0,3}#{1,6} ").replace("|lheading", "").replace("table", T.gfm.table).replace("blockquote", " {0,3}>").replace("fences", " {0,3}(?:`{3,}(?=[^`\\n]*\\n)|~{3,})[^\\n]*\\n").replace("list", " {0,3}(?:[*+-]|1[.)]) ").replace("html", "</?(?:tag)(?: +|\\n|/?>)|<(?:script|pre|style|textarea|!--)").replace("tag", T._tag).getRegex(), T.pedantic = X({}, T.normal, {
        html: R(`^ *(?:comment *(?:\\n|\\s*$)|<(tag)[\\s\\S]+?</\\1> *(?:\\n{2,}|\\s*$)|<tag(?:"[^"]*"|'[^']*'|\\s[^'"/>\\s]*)*?/?> *(?:\\n{2,}|\\s*$))`).replace("comment", T._comment).replace(/tag/g, "(?!(?:a|em|strong|small|s|cite|q|dfn|abbr|data|time|code|var|samp|kbd|sub|sup|i|b|u|mark|ruby|rt|rp|bdi|bdo|span|br|wbr|ins|del|img)\\b)\\w+(?!:|[^\\w\\s@]*@)\\b").getRegex(),
        def: /^ *\[([^\]]+)\]: *<?([^\s>]+)>?(?: +(["(][^\n]+[")]))? *(?:\n+|$)/,
        heading: /^(#{1,6})(.*)(?:\n+|$)/,
        fences: be,
        paragraph: R(T.normal._paragraph).replace("hr", T.hr).replace("heading", ` *#{1,6} *[^
]`).replace("lheading", T.lheading).replace("blockquote", " {0,3}>").replace("|fences", "").replace("|list", "").replace("|html", "").getRegex()
    });
    const x = {
        escape: /^\\([!"#$%&'()*+,\-./:;<=>?@\[\]\\^_`{|}~])/,
        autolink: /^<(scheme:[^\s\x00-\x1f<>]*|email)>/,
        url: be,
        tag: "^comment|^</[a-zA-Z][\\w:-]*\\s*>|^<[a-zA-Z][\\w-]*(?:attribute)*?\\s*/?>|^<\\?[\\s\\S]*?\\?>|^<![a-zA-Z]+\\s[\\s\\S]*?>|^<!\\[CDATA\\[[\\s\\S]*?\\]\\]>",
        link: /^!?\[(label)\]\(\s*(href)(?:\s+(title))?\s*\)/,
        reflink: /^!?\[(label)\]\[(ref)\]/,
        nolink: /^!?\[(ref)\](?:\[\])?/,
        reflinkSearch: "reflink|nolink(?!\\()",
        emStrong: {
            lDelim: /^(?:\*+(?:([punct_])|[^\s*]))|^_+(?:([punct*])|([^\s_]))/,
            rDelimAst: /^[^_*]*?\_\_[^_*]*?\*[^_*]*?(?=\_\_)|[^*]+(?=[^*])|[punct_](\*+)(?=[\s]|$)|[^punct*_\s](\*+)(?=[punct_\s]|$)|[punct_\s](\*+)(?=[^punct*_\s])|[\s](\*+)(?=[punct_])|[punct_](\*+)(?=[punct_])|[^punct*_\s](\*+)(?=[^punct*_\s])/,
            rDelimUnd: /^[^_*]*?\*\*[^_*]*?\_[^_*]*?(?=\*\*)|[^_]+(?=[^_])|[punct*](\_+)(?=[\s]|$)|[^punct*_\s](\_+)(?=[punct*\s]|$)|[punct*\s](\_+)(?=[^punct*_\s])|[\s](\_+)(?=[punct*])|[punct*](\_+)(?=[punct*])/
        },
        code: /^(`+)([^`]|[^`][\s\S]*?[^`])\1(?!`)/,
        br: /^( {2,}|\\)\n(?!\s*$)/,
        del: be,
        text: /^(`+|[^`])(?:(?= {2,}\n)|[\s\S]*?(?:(?=[\\<!\[`*_]|\b_|$)|[^ ](?= {2,}\n)))/,
        punctuation: /^([\spunctuation])/
    };
    x._punctuation = "!\"#$%&'()+\\-.,/:;<=>?@\\[\\]`^{|}~", x.punctuation = R(x.punctuation).replace(/punctuation/g, x._punctuation).getRegex(), x.blockSkip = /\[[^\]]*?\]\([^\)]*?\)|`[^`]*?`|<[^>]*?>/g, x.escapedEmSt = /\\\*|\\_/g, x._comment = R(T._comment).replace("(?:-->|$)", "-->").getRegex(), x.emStrong.lDelim = R(x.emStrong.lDelim).replace(/punct/g, x._punctuation).getRegex(), x.emStrong.rDelimAst = R(x.emStrong.rDelimAst, "g").replace(/punct/g, x._punctuation).getRegex(), x.emStrong.rDelimUnd = R(x.emStrong.rDelimUnd, "g").replace(/punct/g, x._punctuation).getRegex(), x._escapes = /\\([!"#$%&'()*+,\-./:;<=>?@\[\]\\^_`{|}~])/g, x._scheme = /[a-zA-Z][a-zA-Z0-9+.-]{1,31}/, x._email = /[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+(@)[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)+(?![-_])/, x.autolink = R(x.autolink).replace("scheme", x._scheme).replace("email", x._email).getRegex(), x._attribute = /\s+[a-zA-Z:_][\w.:-]*(?:\s*=\s*"[^"]*"|\s*=\s*'[^']*'|\s*=\s*[^\s"'=<>`]+)?/, x.tag = R(x.tag).replace("comment", x._comment).replace("attribute", x._attribute).getRegex(), x._label = /(?:\[(?:\\.|[^\[\]\\])*\]|\\.|`[^`]*`|[^\[\]\\`])*?/, x._href = /<(?:\\.|[^\n<>\\])+>|[^\s\x00-\x1f]*/, x._title = /"(?:\\"?|[^"\\])*"|'(?:\\'?|[^'\\])*'|\((?:\\\)?|[^)\\])*\)/, x.link = R(x.link).replace("label", x._label).replace("href", x._href).replace("title", x._title).getRegex(), x.reflink = R(x.reflink).replace("label", x._label).replace("ref", T._label).getRegex(), x.nolink = R(x.nolink).replace("ref", T._label).getRegex(), x.reflinkSearch = R(x.reflinkSearch, "g").replace("reflink", x.reflink).replace("nolink", x.nolink).getRegex(), x.normal = X({}, x), x.pedantic = X({}, x.normal, {
        strong: {
            start: /^__|\*\*/,
            middle: /^__(?=\S)([\s\S]*?\S)__(?!_)|^\*\*(?=\S)([\s\S]*?\S)\*\*(?!\*)/,
            endAst: /\*\*(?!\*)/g,
            endUnd: /__(?!_)/g
        },
        em: {
            start: /^_|\*/,
            middle: /^()\*(?=\S)([\s\S]*?\S)\*(?!\*)|^_(?=\S)([\s\S]*?\S)_(?!_)/,
            endAst: /\*(?!\*)/g,
            endUnd: /_(?!_)/g
        },
        link: R(/^!?\[(label)\]\((.*?)\)/).replace("label", x._label).getRegex(),
        reflink: R(/^!?\[(label)\]\s*\[([^\]]*)\]/).replace("label", x._label).getRegex()
    }), x.gfm = X({}, x.normal, {
        escape: R(x.escape).replace("])", "~|])").getRegex(),
        _extended_email: /[A-Za-z0-9._+-]+(@)[a-zA-Z0-9-_]+(?:\.[a-zA-Z0-9-_]*[a-zA-Z0-9])+(?![-_])/,
        url: /^((?:ftp|https?):\/\/|www\.)(?:[a-zA-Z0-9\-]+\.?)+[^\s<]*|^email/,
        _backpedal: /(?:[^?!.,:;*_~()&]+|\([^)]*\)|&(?![a-zA-Z0-9]+;$)|[?!.,:;*_~)]+(?!$))+/,
        del: /^(~~?)(?=[^\s~])([\s\S]*?[^\s~])\1(?=[^~]|$)/,
        text: /^([`~]+|[^`~])(?:(?= {2,}\n)|(?=[a-zA-Z0-9.!#$%&'*+\/=?_`{\|}~-]+@)|[\s\S]*?(?:(?=[\\<!\[`*~_]|\b_|https?:\/\/|ftp:\/\/|www\.|$)|[^ ](?= {2,}\n)|[^a-zA-Z0-9.!#$%&'*+\/=?_`{\|}~-](?=[a-zA-Z0-9.!#$%&'*+\/=?_`{\|}~-]+@)))/
    }), x.gfm.url = R(x.gfm.url, "i").replace("email", x.gfm._extended_email).getRegex(), x.breaks = X({}, x.gfm, {
        br: R(x.br).replace("{2,}", "*").getRegex(),
        text: R(x.gfm.text).replace("\\b_", "\\b_| {2,}\\n").replace(/\{2,\}/g, "*").getRegex()
    });

    function sn(i) {
        return i.replace(/---/g, "\u2014").replace(/--/g, "\u2013").replace(/(^|[-\u2014/(\[{"\s])'/g, "$1\u2018").replace(/'/g, "\u2019").replace(/(^|[-\u2014/(\[{\u2018\s])"/g, "$1\u201C").replace(/"/g, "\u201D").replace(/\.{3}/g, "\u2026")
    }

    function at(i) {
        let t = "", e, n;
        const l = i.length;
        for (e = 0; e < l; e++) n = i.charCodeAt(e), Math.random() > .5 && (n = "x" + n.toString(16)), t += "&#" + n + ";";
        return t
    }

    class ee {
        constructor(t) {
            this.tokens = [], this.tokens.links = Object.create(null), this.options = t || ae, this.options.tokenizer = this.options.tokenizer || new De, this.tokenizer = this.options.tokenizer, this.tokenizer.options = this.options, this.tokenizer.lexer = this, this.inlineQueue = [], this.state = {
                inLink: !1,
                inRawBlock: !1,
                top: !0
            };
            const e = {block: T.normal, inline: x.normal};
            this.options.pedantic ? (e.block = T.pedantic, e.inline = x.pedantic) : this.options.gfm && (e.block = T.gfm, this.options.breaks ? e.inline = x.breaks : e.inline = x.gfm), this.tokenizer.rules = e
        }

        static get rules() {
            return {block: T, inline: x}
        }

        static lex(t, e) {
            return new ee(e).lex(t)
        }

        static lexInline(t, e) {
            return new ee(e).inlineTokens(t)
        }

        lex(t) {
            t = t.replace(/\r\n|\r/g, `
`), this.blockTokens(t, this.tokens);
            let e;
            for (; e = this.inlineQueue.shift();) this.inlineTokens(e.src, e.tokens);
            return this.tokens
        }

        blockTokens(t, e = []) {
            this.options.pedantic ? t = t.replace(/\t/g, "    ").replace(/^ +$/gm, "") : t = t.replace(/^( *)(\t+)/gm, (s, d, h) => d + "    ".repeat(h.length));
            let n, l, o, a;
            for (; t;) if (!(this.options.extensions && this.options.extensions.block && this.options.extensions.block.some(s => (n = s.call({lexer: this}, t, e)) ? (t = t.substring(n.raw.length), e.push(n), !0) : !1))) {
                if (n = this.tokenizer.space(t)) {
                    t = t.substring(n.raw.length), n.raw.length === 1 && e.length > 0 ? e[e.length - 1].raw += `
` : e.push(n);
                    continue
                }
                if (n = this.tokenizer.code(t)) {
                    t = t.substring(n.raw.length), l = e[e.length - 1], l && (l.type === "paragraph" || l.type === "text") ? (l.raw += `
` + n.raw, l.text += `
` + n.text, this.inlineQueue[this.inlineQueue.length - 1].src = l.text) : e.push(n);
                    continue
                }
                if (n = this.tokenizer.fences(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.heading(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.hr(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.blockquote(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.list(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.html(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.def(t)) {
                    t = t.substring(n.raw.length), l = e[e.length - 1], l && (l.type === "paragraph" || l.type === "text") ? (l.raw += `
` + n.raw, l.text += `
` + n.raw, this.inlineQueue[this.inlineQueue.length - 1].src = l.text) : this.tokens.links[n.tag] || (this.tokens.links[n.tag] = {
                        href: n.href,
                        title: n.title
                    });
                    continue
                }
                if (n = this.tokenizer.table(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.lheading(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (o = t, this.options.extensions && this.options.extensions.startBlock) {
                    let s = 1 / 0;
                    const d = t.slice(1);
                    let h;
                    this.options.extensions.startBlock.forEach(function (u) {
                        h = u.call({lexer: this}, d), typeof h == "number" && h >= 0 && (s = Math.min(s, h))
                    }), s < 1 / 0 && s >= 0 && (o = t.substring(0, s + 1))
                }
                if (this.state.top && (n = this.tokenizer.paragraph(o))) {
                    l = e[e.length - 1], a && l.type === "paragraph" ? (l.raw += `
` + n.raw, l.text += `
` + n.text, this.inlineQueue.pop(), this.inlineQueue[this.inlineQueue.length - 1].src = l.text) : e.push(n), a = o.length !== t.length, t = t.substring(n.raw.length);
                    continue
                }
                if (n = this.tokenizer.text(t)) {
                    t = t.substring(n.raw.length), l = e[e.length - 1], l && l.type === "text" ? (l.raw += `
` + n.raw, l.text += `
` + n.text, this.inlineQueue.pop(), this.inlineQueue[this.inlineQueue.length - 1].src = l.text) : e.push(n);
                    continue
                }
                if (t) {
                    const s = "Infinite loop on byte: " + t.charCodeAt(0);
                    if (this.options.silent) {
                        console.error(s);
                        break
                    } else throw new Error(s)
                }
            }
            return this.state.top = !0, e
        }

        inline(t, e) {
            this.inlineQueue.push({src: t, tokens: e})
        }

        inlineTokens(t, e = []) {
            let n, l, o, a = t, s, d, h;
            if (this.tokens.links) {
                const u = Object.keys(this.tokens.links);
                if (u.length > 0) for (; (s = this.tokenizer.rules.inline.reflinkSearch.exec(a)) != null;) u.includes(s[0].slice(s[0].lastIndexOf("[") + 1, -1)) && (a = a.slice(0, s.index) + "[" + rt("a", s[0].length - 2) + "]" + a.slice(this.tokenizer.rules.inline.reflinkSearch.lastIndex))
            }
            for (; (s = this.tokenizer.rules.inline.blockSkip.exec(a)) != null;) a = a.slice(0, s.index) + "[" + rt("a", s[0].length - 2) + "]" + a.slice(this.tokenizer.rules.inline.blockSkip.lastIndex);
            for (; (s = this.tokenizer.rules.inline.escapedEmSt.exec(a)) != null;) a = a.slice(0, s.index) + "++" + a.slice(this.tokenizer.rules.inline.escapedEmSt.lastIndex);
            for (; t;) if (d || (h = ""), d = !1, !(this.options.extensions && this.options.extensions.inline && this.options.extensions.inline.some(u => (n = u.call({lexer: this}, t, e)) ? (t = t.substring(n.raw.length), e.push(n), !0) : !1))) {
                if (n = this.tokenizer.escape(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.tag(t)) {
                    t = t.substring(n.raw.length), l = e[e.length - 1], l && n.type === "text" && l.type === "text" ? (l.raw += n.raw, l.text += n.text) : e.push(n);
                    continue
                }
                if (n = this.tokenizer.link(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.reflink(t, this.tokens.links)) {
                    t = t.substring(n.raw.length), l = e[e.length - 1], l && n.type === "text" && l.type === "text" ? (l.raw += n.raw, l.text += n.text) : e.push(n);
                    continue
                }
                if (n = this.tokenizer.emStrong(t, a, h)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.codespan(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.br(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.del(t)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (n = this.tokenizer.autolink(t, at)) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (!this.state.inLink && (n = this.tokenizer.url(t, at))) {
                    t = t.substring(n.raw.length), e.push(n);
                    continue
                }
                if (o = t, this.options.extensions && this.options.extensions.startInline) {
                    let u = 1 / 0;
                    const g = t.slice(1);
                    let k;
                    this.options.extensions.startInline.forEach(function (f) {
                        k = f.call({lexer: this}, g), typeof k == "number" && k >= 0 && (u = Math.min(u, k))
                    }), u < 1 / 0 && u >= 0 && (o = t.substring(0, u + 1))
                }
                if (n = this.tokenizer.inlineText(o, sn)) {
                    t = t.substring(n.raw.length), n.raw.slice(-1) !== "_" && (h = n.raw.slice(-1)), d = !0, l = e[e.length - 1], l && l.type === "text" ? (l.raw += n.raw, l.text += n.text) : e.push(n);
                    continue
                }
                if (t) {
                    const u = "Infinite loop on byte: " + t.charCodeAt(0);
                    if (this.options.silent) {
                        console.error(u);
                        break
                    } else throw new Error(u)
                }
            }
            return e
        }
    }

    class _e {
        constructor(t) {
            this.options = t || ae
        }

        code(t, e, n) {
            const l = (e || "").match(/\S*/)[0];
            if (this.options.highlight) {
                const o = this.options.highlight(t, l);
                o != null && o !== t && (n = !0, t = o)
            }
            return t = t.replace(/\n$/, "") + `
`, l ? '<pre><code class="' + this.options.langPrefix + U(l, !0) + '">' + (n ? t : U(t, !0)) + `</code></pre>
` : "<pre><code>" + (n ? t : U(t, !0)) + `</code></pre>
`
        }

        blockquote(t) {
            return `<blockquote>
${t}</blockquote>
`
        }

        html(t) {
            return t
        }

        heading(t, e, n, l) {
            if (this.options.headerIds) {
                const o = this.options.headerPrefix + l.slug(n);
                return `<h${e} id="${o}">${t}</h${e}>
`
            }
            return `<h${e}>${t}</h${e}>
`
        }

        hr() {
            return this.options.xhtml ? `<hr/>
` : `<hr>
`
        }

        list(t, e, n) {
            const l = e ? "ol" : "ul", o = e && n !== 1 ? ' start="' + n + '"' : "";
            return "<" + l + o + `>
` + t + "</" + l + `>
`
        }

        listitem(t) {
            return `<li>${t}</li>
`
        }

        checkbox(t) {
            return "<input " + (t ? 'checked="" ' : "") + 'disabled="" type="checkbox"' + (this.options.xhtml ? " /" : "") + "> "
        }

        paragraph(t) {
            return `<p>${t}</p>
`
        }

        table(t, e) {
            return e && (e = `<tbody>${e}</tbody>`), `<table>
<thead>
` + t + `</thead>
` + e + `</table>
`
        }

        tablerow(t) {
            return `<tr>
${t}</tr>
`
        }

        tablecell(t, e) {
            const n = e.header ? "th" : "td";
            return (e.align ? `<${n} align="${e.align}">` : `<${n}>`) + t + `</${n}>
`
        }

        strong(t) {
            return `<strong>${t}</strong>`
        }

        em(t) {
            return `<em>${t}</em>`
        }

        codespan(t) {
            return `<code>${t}</code>`
        }

        br() {
            return this.options.xhtml ? "<br/>" : "<br>"
        }

        del(t) {
            return `<del>${t}</del>`
        }

        link(t, e, n) {
            if (t = nt(this.options.sanitize, this.options.baseUrl, t), t === null) return n;
            let l = '<a href="' + U(t) + '"';
            return e && (l += ' title="' + e + '"'), l += ">" + n + "</a>", l
        }

        image(t, e, n) {
            if (t = nt(this.options.sanitize, this.options.baseUrl, t), t === null) return n;
            let l = `<img src="${t}" alt="${n}"`;
            return e && (l += ` title="${e}"`), l += this.options.xhtml ? "/>" : ">", l
        }

        text(t) {
            return t
        }
    }

    class st {
        strong(t) {
            return t
        }

        em(t) {
            return t
        }

        codespan(t) {
            return t
        }

        del(t) {
            return t
        }

        html(t) {
            return t
        }

        text(t) {
            return t
        }

        link(t, e, n) {
            return "" + n
        }

        image(t, e, n) {
            return "" + n
        }

        br() {
            return ""
        }
    }

    class ct {
        constructor() {
            this.seen = {}
        }

        serialize(t) {
            return t.toLowerCase().trim().replace(/<[!\/a-z].*?>/ig, "").replace(/[\u2000-\u206F\u2E00-\u2E7F\\'!"#$%&()*+,./:;<=>?@[\]^`{|}~]/g, "").replace(/\s/g, "-")
        }

        getNextSafeSlug(t, e) {
            let n = t, l = 0;
            if (this.seen.hasOwnProperty(n)) {
                l = this.seen[t];
                do l++, n = t + "-" + l; while (this.seen.hasOwnProperty(n))
            }
            return e || (this.seen[t] = l, this.seen[n] = 0), n
        }

        slug(t, e = {}) {
            const n = this.serialize(t);
            return this.getNextSafeSlug(n, e.dryrun)
        }
    }

    class te {
        constructor(t) {
            this.options = t || ae, this.options.renderer = this.options.renderer || new _e, this.renderer = this.options.renderer, this.renderer.options = this.options, this.textRenderer = new st, this.slugger = new ct
        }

        static parse(t, e) {
            return new te(e).parse(t)
        }

        static parseInline(t, e) {
            return new te(e).parseInline(t)
        }

        parse(t, e = !0) {
            let n = "", l, o, a, s, d, h, u, g, k, f, $, v, w, b, y, E, L, F, O;
            const I = t.length;
            for (l = 0; l < I; l++) {
                if (f = t[l], this.options.extensions && this.options.extensions.renderers && this.options.extensions.renderers[f.type] && (O = this.options.extensions.renderers[f.type].call({parser: this}, f), O !== !1 || !["space", "hr", "heading", "code", "table", "blockquote", "list", "html", "paragraph", "text"].includes(f.type))) {
                    n += O || "";
                    continue
                }
                switch (f.type) {
                    case "space":
                        continue;
                    case "hr": {
                        n += this.renderer.hr();
                        continue
                    }
                    case "heading": {
                        n += this.renderer.heading(this.parseInline(f.tokens), f.depth, tt(this.parseInline(f.tokens, this.textRenderer)), this.slugger);
                        continue
                    }
                    case "code": {
                        n += this.renderer.code(f.text, f.lang, f.escaped);
                        continue
                    }
                    case "table": {
                        for (g = "", u = "", s = f.header.length, o = 0; o < s; o++) u += this.renderer.tablecell(this.parseInline(f.header[o].tokens), {
                            header: !0,
                            align: f.align[o]
                        });
                        for (g += this.renderer.tablerow(u), k = "", s = f.rows.length, o = 0; o < s; o++) {
                            for (h = f.rows[o], u = "", d = h.length, a = 0; a < d; a++) u += this.renderer.tablecell(this.parseInline(h[a].tokens), {
                                header: !1,
                                align: f.align[a]
                            });
                            k += this.renderer.tablerow(u)
                        }
                        n += this.renderer.table(g, k);
                        continue
                    }
                    case "blockquote": {
                        k = this.parse(f.tokens), n += this.renderer.blockquote(k);
                        continue
                    }
                    case "list": {
                        for ($ = f.ordered, v = f.start, w = f.loose, s = f.items.length, k = "", o = 0; o < s; o++) y = f.items[o], E = y.checked, L = y.task, b = "", y.task && (F = this.renderer.checkbox(E), w ? y.tokens.length > 0 && y.tokens[0].type === "paragraph" ? (y.tokens[0].text = F + " " + y.tokens[0].text, y.tokens[0].tokens && y.tokens[0].tokens.length > 0 && y.tokens[0].tokens[0].type === "text" && (y.tokens[0].tokens[0].text = F + " " + y.tokens[0].tokens[0].text)) : y.tokens.unshift({
                            type: "text",
                            text: F
                        }) : b += F), b += this.parse(y.tokens, w), k += this.renderer.listitem(b, L, E);
                        n += this.renderer.list(k, $, v);
                        continue
                    }
                    case "html": {
                        n += this.renderer.html(f.text);
                        continue
                    }
                    case "paragraph": {
                        n += this.renderer.paragraph(this.parseInline(f.tokens));
                        continue
                    }
                    case "text": {
                        for (k = f.tokens ? this.parseInline(f.tokens) : f.text; l + 1 < I && t[l + 1].type === "text";) f = t[++l], k += `
` + (f.tokens ? this.parseInline(f.tokens) : f.text);
                        n += e ? this.renderer.paragraph(k) : k;
                        continue
                    }
                    default: {
                        const m = 'Token with "' + f.type + '" type was not found.';
                        if (this.options.silent) {
                            console.error(m);
                            return
                        } else throw new Error(m)
                    }
                }
            }
            return n
        }

        parseInline(t, e) {
            e = e || this.renderer;
            let n = "", l, o, a;
            const s = t.length;
            for (l = 0; l < s; l++) {
                if (o = t[l], this.options.extensions && this.options.extensions.renderers && this.options.extensions.renderers[o.type] && (a = this.options.extensions.renderers[o.type].call({parser: this}, o), a !== !1 || !["escape", "html", "link", "image", "strong", "em", "codespan", "br", "del", "text"].includes(o.type))) {
                    n += a || "";
                    continue
                }
                switch (o.type) {
                    case "escape": {
                        n += e.text(o.text);
                        break
                    }
                    case "html": {
                        n += e.html(o.text);
                        break
                    }
                    case "link": {
                        n += e.link(o.href, o.title, this.parseInline(o.tokens, e));
                        break
                    }
                    case "image": {
                        n += e.image(o.href, o.title, o.text);
                        break
                    }
                    case "strong": {
                        n += e.strong(this.parseInline(o.tokens, e));
                        break
                    }
                    case "em": {
                        n += e.em(this.parseInline(o.tokens, e));
                        break
                    }
                    case "codespan": {
                        n += e.codespan(o.text);
                        break
                    }
                    case "br": {
                        n += e.br();
                        break
                    }
                    case "del": {
                        n += e.del(this.parseInline(o.tokens, e));
                        break
                    }
                    case "text": {
                        n += e.text(o.text);
                        break
                    }
                    default: {
                        const d = 'Token with "' + o.type + '" type was not found.';
                        if (this.options.silent) {
                            console.error(d);
                            return
                        } else throw new Error(d)
                    }
                }
            }
            return n
        }
    }

    function V(i, t, e) {
        if (typeof i == "undefined" || i === null) throw new Error("marked(): input parameter is undefined or null");
        if (typeof i != "string") throw new Error("marked(): input parameter is of type " + Object.prototype.toString.call(i) + ", string expected");
        if (typeof t == "function" && (e = t, t = null), t = X({}, V.defaults, t || {}), lt(t), e) {
            const n = t.highlight;
            let l;
            try {
                l = ee.lex(i, t)
            } catch (s) {
                return e(s)
            }
            const o = function (s) {
                let d;
                if (!s) try {
                    t.walkTokens && V.walkTokens(l, t.walkTokens), d = te.parse(l, t)
                } catch (h) {
                    s = h
                }
                return t.highlight = n, s ? e(s) : e(null, d)
            };
            if (!n || n.length < 3 || (delete t.highlight, !l.length)) return o();
            let a = 0;
            V.walkTokens(l, function (s) {
                s.type === "code" && (a++, setTimeout(() => {
                    n(s.text, s.lang, function (d, h) {
                        if (d) return o(d);
                        h != null && h !== s.text && (s.text = h, s.escaped = !0), a--, a === 0 && o()
                    })
                }, 0))
            }), a === 0 && o();
            return
        }
        try {
            const n = ee.lex(i, t);
            return t.walkTokens && V.walkTokens(n, t.walkTokens), te.parse(n, t)
        } catch (n) {
            if (n.message += `
Please report this to https://github.com/markedjs/marked.`, t.silent) return "<p>An error occurred:</p><pre>" + U(n.message + "", !0) + "</pre>";
            throw n
        }
    }

    V.options = V.setOptions = function (i) {
        return X(V.defaults, i), qt(V.defaults), V
    }, V.getDefaults = Je, V.defaults = ae, V.use = function (...i) {
        const t = X({}, ...i), e = V.defaults.extensions || {renderers: {}, childTokens: {}};
        let n;
        i.forEach(l => {
            if (l.extensions && (n = !0, l.extensions.forEach(o => {
                if (!o.name) throw new Error("extension name required");
                if (o.renderer) {
                    const a = e.renderers ? e.renderers[o.name] : null;
                    a ? e.renderers[o.name] = function (...s) {
                        let d = o.renderer.apply(this, s);
                        return d === !1 && (d = a.apply(this, s)), d
                    } : e.renderers[o.name] = o.renderer
                }
                if (o.tokenizer) {
                    if (!o.level || o.level !== "block" && o.level !== "inline") throw new Error("extension level must be 'block' or 'inline'");
                    e[o.level] ? e[o.level].unshift(o.tokenizer) : e[o.level] = [o.tokenizer], o.start && (o.level === "block" ? e.startBlock ? e.startBlock.push(o.start) : e.startBlock = [o.start] : o.level === "inline" && (e.startInline ? e.startInline.push(o.start) : e.startInline = [o.start]))
                }
                o.childTokens && (e.childTokens[o.name] = o.childTokens)
            })), l.renderer) {
                const o = V.defaults.renderer || new _e;
                for (const a in l.renderer) {
                    const s = o[a];
                    o[a] = (...d) => {
                        let h = l.renderer[a].apply(o, d);
                        return h === !1 && (h = s.apply(o, d)), h
                    }
                }
                t.renderer = o
            }
            if (l.tokenizer) {
                const o = V.defaults.tokenizer || new De;
                for (const a in l.tokenizer) {
                    const s = o[a];
                    o[a] = (...d) => {
                        let h = l.tokenizer[a].apply(o, d);
                        return h === !1 && (h = s.apply(o, d)), h
                    }
                }
                t.tokenizer = o
            }
            if (l.walkTokens) {
                const o = V.defaults.walkTokens;
                t.walkTokens = function (a) {
                    l.walkTokens.call(this, a), o && o.call(this, a)
                }
            }
            n && (t.extensions = e), V.setOptions(t)
        })
    }, V.walkTokens = function (i, t) {
        for (const e of i) switch (t.call(V, e), e.type) {
            case "table": {
                for (const n of e.header) V.walkTokens(n.tokens, t);
                for (const n of e.rows) for (const l of n) V.walkTokens(l.tokens, t);
                break
            }
            case "list": {
                V.walkTokens(e.items, t);
                break
            }
            default:
                V.defaults.extensions && V.defaults.extensions.childTokens && V.defaults.extensions.childTokens[e.type] ? V.defaults.extensions.childTokens[e.type].forEach(function (n) {
                    V.walkTokens(e[n], t)
                }) : e.tokens && V.walkTokens(e.tokens, t)
        }
    }, V.parseInline = function (i, t) {
        if (typeof i == "undefined" || i === null) throw new Error("marked.parseInline(): input parameter is undefined or null");
        if (typeof i != "string") throw new Error("marked.parseInline(): input parameter is of type " + Object.prototype.toString.call(i) + ", string expected");
        t = X({}, V.defaults, t || {}), lt(t);
        try {
            const e = ee.lexInline(i, t);
            return t.walkTokens && V.walkTokens(e, t.walkTokens), te.parseInline(e, t)
        } catch (e) {
            if (e.message += `
Please report this to https://github.com/markedjs/marked.`, t.silent) return "<p>An error occurred:</p><pre>" + U(e.message + "", !0) + "</pre>";
            throw e
        }
    }, V.Parser = te, V.parser = te.parse, V.Renderer = _e, V.TextRenderer = st, V.Lexer = ee, V.lexer = ee.lex, V.Tokenizer = De, V.Slugger = ct, V.parse = V, te.parse, ee.lex;
    var cn = function () {
            var i = document.getSelection();
            if (!i.rangeCount) return function () {
            };
            for (var t = document.activeElement, e = [], n = 0; n < i.rangeCount; n++) e.push(i.getRangeAt(n));
            switch (t.tagName.toUpperCase()) {
                case "INPUT":
                case "TEXTAREA":
                    t.blur();
                    break;
                default:
                    t = null;
                    break
            }
            return i.removeAllRanges(), function () {
                i.type === "Caret" && i.removeAllRanges(), i.rangeCount || e.forEach(function (l) {
                    i.addRange(l)
                }), t && t.focus()
            }
        }, dn = cn, dt = {"text/plain": "Text", "text/html": "Url", default: "Text"},
        un = "Copy to clipboard: #{key}, Enter";

    function hn(i) {
        var t = (/mac os x/i.test(navigator.userAgent) ? "\u2318" : "Ctrl") + "+C";
        return i.replace(/#{\s*key\s*}/g, t)
    }

    function fn(i, t) {
        var e, n, l, o, a, s, d = !1;
        t || (t = {}), e = t.debug || !1;
        try {
            l = dn(), o = document.createRange(), a = document.getSelection(), s = document.createElement("span"), s.textContent = i, s.style.all = "unset", s.style.position = "fixed", s.style.top = 0, s.style.clip = "rect(0, 0, 0, 0)", s.style.whiteSpace = "pre", s.style.webkitUserSelect = "text", s.style.MozUserSelect = "text", s.style.msUserSelect = "text", s.style.userSelect = "text", s.addEventListener("copy", function (u) {
                if (u.stopPropagation(), t.format) if (u.preventDefault(), typeof u.clipboardData == "undefined") {
                    e && console.warn("unable to use e.clipboardData"), e && console.warn("trying IE specific stuff"), window.clipboardData.clearData();
                    var g = dt[t.format] || dt.default;
                    window.clipboardData.setData(g, i)
                } else u.clipboardData.clearData(), u.clipboardData.setData(t.format, i);
                t.onCopy && (u.preventDefault(), t.onCopy(u.clipboardData))
            }), document.body.appendChild(s), o.selectNodeContents(s), a.addRange(o);
            var h = document.execCommand("copy");
            if (!h) throw new Error("copy command was unsuccessful");
            d = !0
        } catch (u) {
            e && console.error("unable to copy using execCommand: ", u), e && console.warn("trying IE specific stuff");
            try {
                window.clipboardData.setData(t.format || "text", i), t.onCopy && t.onCopy(window.clipboardData), d = !0
            } catch (g) {
                e && console.error("unable to copy using clipboardData: ", g), e && console.error("falling back to prompt"), n = hn("message" in t ? t.message : un), window.prompt(n, i)
            }
        } finally {
            a && (typeof a.removeRange == "function" ? a.removeRange(o) : a.removeAllRanges()), s && document.body.removeChild(s), l()
        }
        return d
    }

    var ue = fn;/*! medium-zoom 1.0.6 | MIT License | https://github.com/francoischalifour/medium-zoom */
    var le = Object.assign || function (i) {
        for (var t = 1; t < arguments.length; t++) {
            var e = arguments[t];
            for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && (i[n] = e[n])
        }
        return i
    }, we = function (t) {
        return t.tagName === "IMG"
    }, mn = function (t) {
        return NodeList.prototype.isPrototypeOf(t)
    }, ve = function (t) {
        return t && t.nodeType === 1
    }, ut = function (t) {
        var e = t.currentSrc || t.src;
        return e.substr(-4).toLowerCase() === ".svg"
    }, ht = function (t) {
        try {
            return Array.isArray(t) ? t.filter(we) : mn(t) ? [].slice.call(t).filter(we) : ve(t) ? [t].filter(we) : typeof t == "string" ? [].slice.call(document.querySelectorAll(t)).filter(we) : []
        } catch {
            throw new TypeError(`The provided selector is invalid.
Expects a CSS selector, a Node element, a NodeList or an array.
See: https://github.com/francoischalifour/medium-zoom`)
        }
    }, gn = function (t) {
        var e = document.createElement("div");
        return e.classList.add("medium-zoom-overlay"), e.style.background = t, e
    }, pn = function (t) {
        var e = t.getBoundingClientRect(), n = e.top, l = e.left, o = e.width, a = e.height, s = t.cloneNode(),
            d = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0,
            h = window.pageXOffset || document.documentElement.scrollLeft || document.body.scrollLeft || 0;
        return s.removeAttribute("id"), s.style.position = "absolute", s.style.top = n + d + "px", s.style.left = l + h + "px", s.style.width = o + "px", s.style.height = a + "px", s.style.transform = "", s
    }, se = function (t, e) {
        var n = le({bubbles: !1, cancelable: !1, detail: void 0}, e);
        if (typeof window.CustomEvent == "function") return new CustomEvent(t, n);
        var l = document.createEvent("CustomEvent");
        return l.initCustomEvent(t, n.bubbles, n.cancelable, n.detail), l
    }, kn = function i(t) {
        var e = arguments.length > 1 && arguments[1] !== void 0 ? arguments[1] : {},
            n = window.Promise || function (N) {
                function p() {
                }

                N(p, p)
            }, l = function (N) {
                var p = N.target;
                if (p === q) {
                    $();
                    return
                }
                E.indexOf(p) !== -1 && v({target: p})
            }, o = function () {
                if (!(F || !m.original)) {
                    var N = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
                    Math.abs(O - N) > I.scrollOffset && setTimeout($, 150)
                }
            }, a = function (N) {
                var p = N.key || N.keyCode;
                (p === "Escape" || p === "Esc" || p === 27) && $()
            }, s = function () {
                var N = arguments.length > 0 && arguments[0] !== void 0 ? arguments[0] : {}, p = N;
                if (N.background && (q.style.background = N.background), N.container && N.container instanceof Object && (p.container = le({}, I.container, N.container)), N.template) {
                    var S = ve(N.template) ? N.template : document.querySelector(N.template);
                    p.template = S
                }
                return I = le({}, I, p), E.forEach(function (z) {
                    z.dispatchEvent(se("medium-zoom:update", {detail: {zoom: j}}))
                }), j
            }, d = function () {
                var N = arguments.length > 0 && arguments[0] !== void 0 ? arguments[0] : {};
                return i(le({}, I, N))
            }, h = function () {
                for (var N = arguments.length, p = Array(N), S = 0; S < N; S++) p[S] = arguments[S];
                var z = p.reduce(function (A, P) {
                    return [].concat(A, ht(P))
                }, []);
                return z.filter(function (A) {
                    return E.indexOf(A) === -1
                }).forEach(function (A) {
                    E.push(A), A.classList.add("medium-zoom-image")
                }), L.forEach(function (A) {
                    var P = A.type, B = A.listener, K = A.options;
                    z.forEach(function (Z) {
                        Z.addEventListener(P, B, K)
                    })
                }), j
            }, u = function () {
                for (var N = arguments.length, p = Array(N), S = 0; S < N; S++) p[S] = arguments[S];
                m.zoomed && $();
                var z = p.length > 0 ? p.reduce(function (A, P) {
                    return [].concat(A, ht(P))
                }, []) : E;
                return z.forEach(function (A) {
                    A.classList.remove("medium-zoom-image"), A.dispatchEvent(se("medium-zoom:detach", {detail: {zoom: j}}))
                }), E = E.filter(function (A) {
                    return z.indexOf(A) === -1
                }), j
            }, g = function (N, p) {
                var S = arguments.length > 2 && arguments[2] !== void 0 ? arguments[2] : {};
                return E.forEach(function (z) {
                    z.addEventListener("medium-zoom:" + N, p, S)
                }), L.push({type: "medium-zoom:" + N, listener: p, options: S}), j
            }, k = function (N, p) {
                var S = arguments.length > 2 && arguments[2] !== void 0 ? arguments[2] : {};
                return E.forEach(function (z) {
                    z.removeEventListener("medium-zoom:" + N, p, S)
                }), L = L.filter(function (z) {
                    return !(z.type === "medium-zoom:" + N && z.listener.toString() === p.toString())
                }), j
            }, f = function () {
                var N = arguments.length > 0 && arguments[0] !== void 0 ? arguments[0] : {}, p = N.target, S = function () {
                    var A = {
                        width: document.documentElement.clientWidth,
                        height: document.documentElement.clientHeight,
                        left: 0,
                        top: 0,
                        right: 0,
                        bottom: 0
                    }, P = void 0, B = void 0;
                    if (I.container) if (I.container instanceof Object) A = le({}, A, I.container), P = A.width - A.left - A.right - I.margin * 2, B = A.height - A.top - A.bottom - I.margin * 2; else {
                        var K = ve(I.container) ? I.container : document.querySelector(I.container),
                            Z = K.getBoundingClientRect(), ce = Z.width, $e = Z.height, Ce = Z.left, Se = Z.top;
                        A = le({}, A, {width: ce, height: $e, left: Ce, top: Se})
                    }
                    P = P || A.width - I.margin * 2, B = B || A.height - I.margin * 2;
                    var ie = m.zoomedHd || m.original, Ee = ut(ie) ? P : ie.naturalWidth || P,
                        Ne = ut(ie) ? B : ie.naturalHeight || B, re = ie.getBoundingClientRect(), Te = re.top, Ve = re.left,
                        fe = re.width, me = re.height, ze = Math.min(Ee, P) / fe, Ae = Math.min(Ne, B) / me,
                        de = Math.min(ze, Ae), Le = (-Ve + (P - fe) / 2 + I.margin + A.left) / de,
                        Ie = (-Te + (B - me) / 2 + I.margin + A.top) / de,
                        ge = "scale(" + de + ") translate3d(" + Le + "px, " + Ie + "px, 0)";
                    m.zoomed.style.transform = ge, m.zoomedHd && (m.zoomedHd.style.transform = ge)
                };
                return new n(function (z) {
                    if (p && E.indexOf(p) === -1) {
                        z(j);
                        return
                    }
                    var A = function ce() {
                        F = !1, m.zoomed.removeEventListener("transitionend", ce), m.original.dispatchEvent(se("medium-zoom:opened", {detail: {zoom: j}})), z(j)
                    };
                    if (m.zoomed) {
                        z(j);
                        return
                    }
                    if (p) m.original = p; else if (E.length > 0) {
                        var P = E;
                        m.original = P[0]
                    } else {
                        z(j);
                        return
                    }
                    if (m.original.dispatchEvent(se("medium-zoom:open", {detail: {zoom: j}})), O = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0, F = !0, m.zoomed = pn(m.original), document.body.appendChild(q), I.template) {
                        var B = ve(I.template) ? I.template : document.querySelector(I.template);
                        m.template = document.createElement("div"), m.template.appendChild(B.content.cloneNode(!0)), document.body.appendChild(m.template)
                    }
                    if (document.body.appendChild(m.zoomed), window.requestAnimationFrame(function () {
                        document.body.classList.add("medium-zoom--opened")
                    }), m.original.classList.add("medium-zoom-image--hidden"), m.zoomed.classList.add("medium-zoom-image--opened"), m.zoomed.addEventListener("click", $), m.zoomed.addEventListener("transitionend", A), m.original.getAttribute("data-zoom-src")) {
                        m.zoomedHd = m.zoomed.cloneNode(), m.zoomedHd.removeAttribute("srcset"), m.zoomedHd.removeAttribute("sizes"), m.zoomedHd.src = m.zoomed.getAttribute("data-zoom-src"), m.zoomedHd.onerror = function () {
                            clearInterval(K), console.warn("Unable to reach the zoom image target " + m.zoomedHd.src), m.zoomedHd = null, S()
                        };
                        var K = setInterval(function () {
                            m.zoomedHd.complete && (clearInterval(K), m.zoomedHd.classList.add("medium-zoom-image--opened"), m.zoomedHd.addEventListener("click", $), document.body.appendChild(m.zoomedHd), S())
                        }, 10)
                    } else if (m.original.hasAttribute("srcset")) {
                        m.zoomedHd = m.zoomed.cloneNode(), m.zoomedHd.removeAttribute("sizes"), m.zoomedHd.removeAttribute("loading");
                        var Z = m.zoomedHd.addEventListener("load", function () {
                            m.zoomedHd.removeEventListener("load", Z), m.zoomedHd.classList.add("medium-zoom-image--opened"), m.zoomedHd.addEventListener("click", $), document.body.appendChild(m.zoomedHd), S()
                        })
                    } else S()
                })
            }, $ = function () {
                return new n(function (N) {
                    if (F || !m.original) {
                        N(j);
                        return
                    }
                    var p = function S() {
                        m.original.classList.remove("medium-zoom-image--hidden"), document.body.removeChild(m.zoomed), m.zoomedHd && document.body.removeChild(m.zoomedHd), document.body.removeChild(q), m.zoomed.classList.remove("medium-zoom-image--opened"), m.template && document.body.removeChild(m.template), F = !1, m.zoomed.removeEventListener("transitionend", S), m.original.dispatchEvent(se("medium-zoom:closed", {detail: {zoom: j}})), m.original = null, m.zoomed = null, m.zoomedHd = null, m.template = null, N(j)
                    };
                    F = !0, document.body.classList.remove("medium-zoom--opened"), m.zoomed.style.transform = "", m.zoomedHd && (m.zoomedHd.style.transform = ""), m.template && (m.template.style.transition = "opacity 150ms", m.template.style.opacity = 0), m.original.dispatchEvent(se("medium-zoom:close", {detail: {zoom: j}})), m.zoomed.addEventListener("transitionend", p)
                })
            }, v = function () {
                var N = arguments.length > 0 && arguments[0] !== void 0 ? arguments[0] : {}, p = N.target;
                return m.original ? $() : f({target: p})
            }, w = function () {
                return I
            }, b = function () {
                return E
            }, y = function () {
                return m.original
            }, E = [], L = [], F = !1, O = 0, I = e, m = {original: null, zoomed: null, zoomedHd: null, template: null};
        Object.prototype.toString.call(t) === "[object Object]" ? I = t : (t || typeof t == "string") && h(t), I = le({
            margin: 0,
            background: "#fff",
            scrollOffset: 40,
            container: null,
            template: null
        }, I);
        var q = gn(I.background);
        document.addEventListener("click", l), document.addEventListener("keyup", a), document.addEventListener("scroll", o), window.addEventListener("resize", $);
        var j = {
            open: f,
            close: $,
            toggle: v,
            update: s,
            clone: d,
            attach: h,
            detach: u,
            on: g,
            off: k,
            getOptions: w,
            getImages: b,
            getZoomedImage: y
        };
        return j
    };

    function bn(i, t) {
        t === void 0 && (t = {});
        var e = t.insertAt;
        if (!(!i || typeof document == "undefined")) {
            var n = document.head || document.getElementsByTagName("head")[0], l = document.createElement("style");
            l.type = "text/css", e === "top" && n.firstChild ? n.insertBefore(l, n.firstChild) : n.appendChild(l), l.styleSheet ? l.styleSheet.cssText = i : l.appendChild(document.createTextNode(i))
        }
    }

    var yn = ".medium-zoom-overlay{position:fixed;top:0;right:0;bottom:0;left:0;opacity:0;transition:opacity .3s;will-change:opacity}.medium-zoom--opened .medium-zoom-overlay{cursor:pointer;cursor:zoom-out;opacity:1}.medium-zoom-image{cursor:pointer;cursor:zoom-in;transition:transform .3s cubic-bezier(.2,0,.2,1)!important}.medium-zoom-image--hidden{visibility:hidden}.medium-zoom-image--opened{position:relative;cursor:pointer;cursor:zoom-out;will-change:transform}";
    bn(yn);
    var wn = kn;
    const he = i => {
        const t = i.value, e = t.substring(0, i.selectionStart), n = t.substring(i.selectionEnd, t.length),
            l = e.lastIndexOf(`
`), o = e.substring(0, l + 1), a = n.indexOf(`
`), s = n.substring(a === -1 ? n.length : a, n.length), d = e.substring(l + 1, e.length), h = n.substring(0, a);
        return {prefixStr: e, subfixStr: n, prefixStrEndRow: o, subfixStrEndRow: s, prefixSupply: d, subfixSupply: h}
    }, vn = (i, t = "", e, n) => {
        var g, k, f, $, v;
        let l = "", o = 0, a = 0, s = !1, d, h;
        const u = (g = M.editorConfig) == null ? void 0 : g.mermaidTemplate;
        if (/^h[1-6]{1}$/.test(i)) {
            const w = i.replace(/^h(\d)/, (b, y) => new Array(Number(y)).fill("#", 0, y).join(""));
            l = `${w} ${t}`, o = w.length + 1
        } else if (i === "prettier") {
            const w = window.prettier || ((f = (k = M.editorExtensions) == null ? void 0 : k.prettier) == null ? void 0 : f.prettierInstance),
                b = window.prettierPlugins || [(v = ($ = M.editorExtensions) == null ? void 0 : $.prettier) == null ? void 0 : v.parserMarkdownInstance];
            return !w || b[0] === void 0 ? (C.emit(n.editorId, "errorCatcher", {
                name: "prettier",
                message: "prettier is undefined"
            }), e.value) : w.format(e.value, {parser: "markdown", plugins: b})
        } else switch (i) {
            case "bold": {
                l = `**${t}**`, o = 2, a = -2;
                break
            }
            case "underline": {
                l = `<u>${t}</u>`, o = 3, a = -4;
                break
            }
            case "italic": {
                l = `*${t}*`, o = 1, a = -1;
                break
            }
            case "strikeThrough": {
                l = `~${t}~`, o = 1, a = -1;
                break
            }
            case "sub": {
                l = `<sub>${t}</sub>`, o = 5, a = -6;
                break
            }
            case "sup": {
                l = `<sup>${t}</sup>`, o = 5, a = -6;
                break
            }
            case "codeRow": {
                l = "`" + t + "`", o = 1, a = -1;
                break
            }
            case "quote": {
                l = `> ${t}`, o = 2;
                break
            }
            case "orderedList": {
                l = `1. ${t}`, o = 3;
                break
            }
            case "unorderedList": {
                l = `- ${t}`, o = 2;
                break
            }
            case "code": {
                l = "```language\n" + t + "\n```\n", o = 3, a = 11 - l.length, s = !0;
                break
            }
            case "table": {
                l = "|";
                const {selectedShape: w = {x: 1, y: 1}} = n, {x: b, y} = w;
                for (let E = 0; E <= y; E++) l += " col |";
                l += `
|`;
                for (let E = 0; E <= y; E++) l += " - |";
                for (let E = 0; E <= b; E++) {
                    l += `
|`;
                    for (let L = 0; L <= y; L++) l += " content |"
                }
                o = 2, a = 5 - l.length, s = !0;
                break
            }
            case "link": {
                const {desc: w, url: b} = n;
                l = `[${w}](${b})`;
                break
            }
            case "image": {
                const {desc: w, url: b, urls: y} = n;
                y instanceof Array ? l = y.reduce((E, L) => E + `![${w}](${L})
`, "") : l = `![${w}](${b})
`;
                break
            }
            case "tab": {
                const {tabWidth: w = 2} = n, b = new Array(w).fill(" ").join("");
                if (t === "") l = b; else if (/\n/.test(t)) {
                    const {prefixStr: y, subfixStr: E, prefixSupply: L, subfixSupply: F} = he(e);
                    l = `${L}${t}${F}`.split(`
`).map(m => `${b}${m}`).join(`
`), d = y.substring(0, y.length - L.length), h = E.substring(F.length, E.length), s = !0, o = w, a = -L.length - F.length
                } else {
                    const E = e.value.substring(0, e.selectionStart);
                    /\n$/.test(E) || E === "" ? (l = `${b}${t}`, s = !0) : l = b
                }
                break
            }
            case "shiftTab": {
                const {tabWidth: w = 2} = n, {prefixStr: b, prefixStrEndRow: y, subfixStrEndRow: E, prefixSupply: L, subfixSupply: F} = he(e),
                    O = new RegExp(`^\\s{${w}}`), I = (m = !1, q = !1) => {
                        const j = `${L}${t}${F}`;
                        if (O.test(j)) {
                            const D = b.length - (q ? 0 : w), N = m ? D + t.length - w : D;
                            return J(e, D, N), `${y}${j.replace(O, "")}${E}`
                        } else if (/^\s/.test(j)) {
                            const D = j.replace(/^\s/, ""), N = j.length - D.length, p = e.selectionStart - (q ? 0 : N),
                                S = m ? p + t.length - N : p;
                            return J(e, p, S), `${y}${D}${E}`
                        } else l = t
                    };
                if (t === "") {
                    const m = I();
                    if (m) return m
                } else if (/\n/.test(t)) {
                    const q = `${L}${t}${F}`.split(`
`);
                    let [j, D] = [0, 0];
                    const N = q.map((p, S) => {
                        if (O.test(p)) return S === 0 && (j = w), D += w, p.replace(O, "");
                        if (/^\s/.test(p)) {
                            const z = p.replace(/^\s/, "");
                            return D += p.length - z.length, z
                        }
                        return p
                    }).join(`
`);
                    return J(e, e.selectionStart - j, e.selectionEnd - D), `${y}${N}${E}`
                } else {
                    const m = I(!0, !0);
                    if (m) return m
                }
                break
            }
            case "ctrlC": {
                const {prefixSupply: w, subfixSupply: b} = he(e);
                return ue(t === "" ? `${w}${b}` : t), e.value
            }
            case "ctrlX": {
                const {prefixStrEndRow: w, subfixStrEndRow: b, prefixStr: y, subfixStr: E, prefixSupply: L, subfixSupply: F} = he(e);
                return t === "" ? (ue(`${L}${F}`), J(e, w.length), `${w}${b.replace(/^\n/, "")}`) : (ue(t), J(e, y.length), `${y}${E}`)
            }
            case "ctrlD": {
                const {prefixStrEndRow: w, subfixStrEndRow: b} = he(e);
                return J(e, w.length), `${w}${b.replace(/^\n/, "")}`
            }
            case "flow": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.flow) || `flowchart TD 
  Start --> Stop`}
\`\`\`
`, o = 2;
                break
            }
            case "sequence": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.sequence) || `sequenceDiagram
  A->>B: hello!
  B-->>A: hi!
  A-)B: bye!`}
\`\`\`
`, o = 2;
                break
            }
            case "gantt": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.gantt) || `gantt
title A Gantt Diagram
dateFormat  YYYY-MM-DD
section Section
A task  :a1, 2014-01-01, 30d
Another task  :after a1, 20d`}
\`\`\`
`, o = 2;
                break
            }
            case "class": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.class) || `classDiagram
  class Animal
  Vehicle <|-- Car`}
\`\`\`
`, o = 2;
                break
            }
            case "state": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.state) || `stateDiagram-v2
  s1 --> s2`}
\`\`\`
`, o = 2;
                break
            }
            case "pie": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.pie) || `pie title Pets adopted by volunteers
  "Dogs" : 386
  "Cats" : 85
  "Rats" : 15`}
\`\`\`
`, o = 2;
                break
            }
            case "relationship": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.relationship) || `erDiagram
  CAR ||--o{ NAMED-DRIVER : allows
  PERSON ||--o{ NAMED-DRIVER : is`}
\`\`\`
`, o = 2;
                break
            }
            case "journey": {
                l = `\`\`\`mermaid
${(u == null ? void 0 : u.journey) || `journey
  title My working day
  section Go to work
    Make tea: 5: Me
    Go upstairs: 3: Me
    Do work: 1: Me, Cat
  section Go home
    Go downstairs: 5: Me
    Sit down: 5: Me`}
\`\`\`
`, o = 2;
                break
            }
            case "katexInline": {
                l = "$$", o = 1, a = -1;
                break
            }
            case "katexBlock": {
                l = `$$

$$
`, o = 1, a = -4;
                break
            }
        }
        return Fe(e, l, {deviationStart: o, deviationEnd: a, select: s, prefixVal: d, subfixVal: h})
    };
    var ft = {
        block(i, t) {
            return {
                name: "KaTexBlockExtension", level: "block", start: e => {
                    var n;
                    return (n = e.match(/\n\$\$\n/)) == null ? void 0 : n.index
                }, tokenizer(e) {
                    if (/^\$\$\n/.test(e) && e.split("$$").length > 2) {
                        const n = Xe(e, "$$");
                        return {type: "KaTexBlockExtension", raw: n[0], text: n[1].trim(), tokens: []}
                    }
                }, renderer(e) {
                    const n = t || typeof window != "undefined" && window.katex;
                    if (n) {
                        const l = n.renderToString(e.text, {throwOnError: !1, displayMode: !0});
                        return `<span class="${i}-katex-block" data-processed>${l}</span>`
                    } else return `<span class="${i}-katex-block">${e.text}</span>`
                }
            }
        }, inline(i, t) {
            return {
                name: "KaTexInlineExtension", level: "inline", start: e => {
                    var n;
                    return (n = e.match(/\$[^\n]*/)) == null ? void 0 : n.index
                }, tokenizer(e) {
                    if (/^\$[^\n]*\$/.test(e)) {
                        const n = Xe(e);
                        return {type: "KaTexInlineExtension", raw: n[0], text: n[1].trim(), tokens: []}
                    }
                }, renderer(e) {
                    const n = t || typeof window != "undefined" && window.katex;
                    if (n) {
                        const l = n.renderToString(e.text, {throwOnError: !1});
                        return `<span class="${i}-katex-inline" data-processed>${l}</span>`
                    } else return `<span class="${i}-katex-inline">${e.text}</span>`
                }
            }
        }
    };
    const xn = (i, t, e) => {
        var f, $;
        const n = r.inject("previewOnly"), l = r.inject("historyLength"), o = r.inject("editorId");
        if (n) return;
        let a = -1;
        const s = {
            list: [{
                content: i.value,
                startPos: ((f = t.value) == null ? void 0 : f.selectionStart) || 0,
                endPos: (($ = t.value) == null ? void 0 : $.selectionEnd) || 0
            }], userUpdated: !0, curr: 0
        }, d = [0, 0];
        let h = d;
        const u = v => {
            var E, L;
            const w = ((E = t.value) == null ? void 0 : E.selectionStart) || 0,
                b = ((L = t.value) == null ? void 0 : L.selectionEnd) || 0;
            s.list[s.curr].startPos = w, s.list[s.curr].endPos = b, s.userUpdated = !1, s.curr = v;
            const y = s.list[s.curr];
            h = [y.startPos, y.endPos], i.onChange(y.content), J(t.value, y.startPos, y.endPos).then(() => {
                C.emit(o, "selectTextChange")
            })
        }, g = v => {
            var y, E;
            clearTimeout(a);
            const w = ((y = t.value) == null ? void 0 : y.selectionStart) || 0,
                b = ((E = t.value) == null ? void 0 : E.selectionEnd) || 0;
            a = setTimeout(() => {
                if (s.userUpdated) {
                    s.curr < s.list.length - 1 && (s.list = s.list.slice(0, s.curr + 1)), s.list.length > l && s.list.shift();
                    const L = s.list.pop() || {startPos: 0, endPos: 0, content: v};
                    L.startPos = h[0], L.endPos = h[1], h = d, Array.prototype.push.call(s.list, L, {
                        content: v,
                        startPos: w,
                        endPos: b
                    }), s.curr = s.list.length - 1
                } else s.userUpdated = !0
            }, 150)
        }, k = v => {
            var w, b;
            (h === d || v) && (h = [(w = t.value) == null ? void 0 : w.selectionStart, (b = t.value) == null ? void 0 : b.selectionEnd])
        };
        r.watch([r.toRef(i, "value"), e], () => {
            e.value && g(i.value)
        }), r.watch(() => i.value, () => {
            C.emit(o, "selectTextChange")
        }, {flush: "post"}), r.onMounted(() => {
            C.on(o, {
                name: "ctrlZ", callback() {
                    u(s.curr - 1 < 0 ? 0 : s.curr - 1)
                }
            }), C.on(o, {
                name: "ctrlShiftZ", callback() {
                    u(s.curr + 1 === s.list.length ? s.curr : s.curr + 1)
                }
            }), C.on(o, {name: "saveHistoryPos", callback: k})
        })
    }, $n = (i, t) => {
        var j, D, N;
        const {markedRenderer: e, markedExtensions: n, markedOptions: l, editorExtensions: o, editorConfig: a} = M,
            s = r.inject("showCodeRowNumber"), d = r.inject("editorId"), h = r.inject("highlight"),
            u = r.inject("previewOnly"), g = (j = o == null ? void 0 : o.highlight) == null ? void 0 : j.instance,
            k = (D = o == null ? void 0 : o.mermaid) == null ? void 0 : D.instance,
            f = (N = o == null ? void 0 : o.katex) == null ? void 0 : N.instance, $ = o == null ? void 0 : o.katex,
            v = r.ref(!1), w = r.ref(!1), b = r.ref([]);
        let y = new V.Renderer;
        const E = y.code;
        y.code = (p, S, z) => {
            var A;
            if (!i.noMermaid && S === "mermaid") {
                const P = `${c}-mermaid-${Date.now().toString(36)}`;
                try {
                    let B = "";
                    return k ? B = k.mermaidAPI.render(P, p) : typeof window != "undefined" && window.mermaid ? B = window.mermaid.mermaidAPI.render(P, p) : B = `<div class="mermaid">${p}</div>`, `<div class="${c}-mermaid">${B}</div>`
                } catch {
                    if (typeof document != "undefined") {
                        const K = document.querySelector(`#${P}`);
                        if (K) {
                            const Z = K.outerHTML;
                            return (A = K.parentElement) == null || A.remove(), Z
                        }
                    }
                    return ""
                }
            }
            return E.call(y, p, S, z)
        }, y.image = (p, S, z) => `<span class="figure"><img src="${p}" title="${S || ""}" alt="${z || ""}" zoom><span class="figcaption">${z || ""}</span></span>`, y.listitem = (p, S) => S ? `<li class="li-task">${p}</li>` : `<li>${p}</li>`;
        const L = y.heading;
        e instanceof Function && (y = e(y));
        const F = y.heading, O = L !== F;
        y.heading = (p, S, z, A) => {
            if (b.value.push({text: z, level: S}), O) return F.call(y, p, S, z, A, b.value.length);
            const P = i.markedHeadingId(z, S, b.value.length);
            return p !== z ? `<h${S} id="${P}">${p}</h${S}>` : `<h${S} id="${P}"><a href="#${P}">${z}</a></h${S}>`
        }, V.setOptions(Y({breaks: !0}, l)), i.noKatex || V.use({extensions: [ft.inline(c, f), ft.block(c, f)]}), g && V.setOptions({
            highlight: (p, S) => {
                let z = "";
                const A = g.getLanguage(S);
                return S && A ? z = g.highlight(p, {
                    language: S,
                    ignoreIllegals: !0
                }).value : z = g.highlightAuto(p).value, s ? Qe(z) : `<span class="code-block">${z}</span>`
            }
        }), n instanceof Array && n.length > 0 && V.use({extensions: n});
        const I = r.ref(i.sanitize(V(i.value || "", {renderer: y}))), m = pe(() => {
            b.value = [];
            const p = i.sanitize(V(i.value || "", {renderer: y}));
            I.value = p, i.onHtmlChanged(p)
        }, (a == null ? void 0 : a.renderDelay) !== void 0 ? a == null ? void 0 : a.renderDelay : u ? 0 : 500);
        r.watch([v, r.toRef(t, "reRender"), r.toRef(t, "mermaidInited"), w, r.toRef(i, "value")], m);
        const q = () => {
            V.setOptions({
                highlight: (p, S) => {
                    let z = "";
                    const A = window.hljs.getLanguage(S);
                    return S && A ? z = window.hljs.highlight(p, {
                        language: S,
                        ignoreIllegals: !0
                    }).value : z = window.hljs.highlightAuto(p).value, s ? Qe(z) : `<span class="code-block">${z}</span>`
                }
            }), v.value = !0
        };
        return r.watch(() => b.value, p => {
            i.onGetCatalog(p), C.emit(d, "catalogChanged", p)
        }), r.onMounted(() => {
            if (!i.noKatex && !f) {
                const p = document.createElement("script");
                p.src = ($ == null ? void 0 : $.js) || Ze.js, p.onload = () => {
                    w.value = !0
                }, p.id = `${c}-katex`;
                const S = document.createElement("link");
                S.rel = "stylesheet", S.href = ($ == null ? void 0 : $.css) || Ze.css, S.id = `${c}-katexCss`, G(p, "katex"), G(S)
            }
            if (!g) {
                const p = document.createElement("link");
                p.rel = "stylesheet", p.href = h.value.css, p.id = `${c}-hlCss`;
                const S = document.createElement("script");
                S.src = h.value.js, S.onload = q, S.id = `${c}-hljs`, G(p), G(S, "hljs")
            }
        }), r.watch(() => h.value.css, p => {
            St(`${c}-hlCss`, "href", p)
        }), r.onMounted(() => {
            C.on(d, {
                name: "pushCatalog", callback() {
                    C.emit(d, "catalogChanged", b.value)
                }
            })
        }), {html: I}
    }, Cn = (i, t, e, n, l) => {
        const o = r.inject("previewOnly"), a = r.inject("usedLanguageText"), s = r.inject("editorId");
        let d = () => {
        }, h = () => {
        };
        const u = () => {
            document.querySelectorAll(`#${s}-preview pre`).forEach(f => {
                var w;
                const $ = ((w = a.value.copyCode) == null ? void 0 : w.text) || "\u590D\u5236\u4EE3\u7801",
                    v = document.createElement("span");
                v.setAttribute("class", "copy-button"), v.innerText = $, v.addEventListener("click", () => {
                    var L, F;
                    const b = ue(f.querySelector("code").innerText),
                        y = ((L = a.value.copyCode) == null ? void 0 : L.successTips) || "\u5DF2\u590D\u5236\uFF01",
                        E = ((F = a.value.copyCode) == null ? void 0 : F.failTips) || "\u5DF2\u590D\u5236\uFF01";
                    v.innerText = b ? y : E, setTimeout(() => {
                        v.innerText = $
                    }, 1500)
                }), f.appendChild(v)
            })
        }, g = () => {
            r.nextTick(() => {
                i.setting.preview && !o && i.scrollAuto && (d(), h()), u()
            })
        }, k = f => {
            f && !o && r.nextTick(() => {
                d(), [h, d] = Ge(e.value, n.value || l.value), h()
            })
        };
        r.watch(() => t.value, g), r.watch(() => i.setting.preview, k), r.watch(() => i.setting.htmlPreview, k), r.watch(() => i.scrollAuto, f => {
            f ? h() : d()
        }), r.onMounted(() => {
            u(), !o && (n.value || l.value) && ([h, d] = Ge(e.value, n.value || l.value)), i.scrollAuto && h()
        })
    }, Sn = (i, t) => {
        const e = r.inject("previewOnly"), n = r.inject("tabWidth"), l = r.inject("editorId"), o = r.ref("");
        r.onMounted(() => {
            var a;
            e || ((a = t.value) == null || a.addEventListener("keypress", s => {
                var d, h, u;
                if (s.key === "Enter") {
                    const g = (d = t.value) == null ? void 0 : d.selectionStart,
                        k = (h = t.value) == null ? void 0 : h.value.substring(0, g),
                        f = (u = t.value) == null ? void 0 : u.value.substring(g),
                        $ = k == null ? void 0 : k.lastIndexOf(`
`), v = k == null ? void 0 : k.substring($ + 1, g);
                    if (/^\d+\.\s|^-\s/.test(v)) if (s.cancelBubble = !0, s.preventDefault(), s.stopPropagation(), /^\d+\.\s+$|^-\s+$/.test(v)) {
                        const w = k == null ? void 0 : k.replace(new RegExp(v + "$"), "");
                        i.onChange(w + f), J(t.value, w == null ? void 0 : w.length)
                    } else if (/^-\s+.+/.test(v)) i.onChange(Fe(t.value, `
- `, {})); else {
                        const w = v == null ? void 0 : v.match(/\d+(?=\.)/), b = w && Number(w[0]) + 1 || 1;
                        i.onChange(Fe(t.value, `
${b}. `, {}))
                    }
                }
            }), C.on(l, {
                name: "replace", callback(s, d = {}) {
                    i.onChange(vn(s, o.value, t.value, Oe(Y({}, d), {tabWidth: n, editorId: l})))
                }
            }))
        }), r.watch(() => i.value, () => {
            o.value = ""
        }), C.on(l, {
            name: "selectTextChange", callback() {
                o.value = Ct(t.value)
            }
        })
    }, En = i => {
        const t = r.inject("theme"), {editorExtensions: e} = M, n = e == null ? void 0 : e.mermaid,
            l = r.reactive({reRender: !1, mermaidInited: !!(n != null && n.instance)}), o = () => {
                i.noMermaid || (n != null && n.instance ? n.instance.initialize({theme: t.value === "dark" ? "dark" : "default"}) : window.mermaid && window.mermaid.initialize({theme: t.value === "dark" ? "dark" : "default"}), l.reRender = !l.reRender)
            };
        r.watch(() => t.value, o);
        let a;
        return r.onMounted(() => {
            !i.noMermaid && !(n != null && n.instance) ? (a = document.createElement("script"), a.src = (n == null ? void 0 : n.js) || yt, a.onload = () => {
                window.mermaid.initialize({
                    theme: t.value === "dark" ? "dark" : "default",
                    logLevel: "Fatal"
                }), l.mermaidInited = !0
            }, a.id = `${c}-mermaid`, G(a, "mermaid")) : i.noMermaid || o()
        }), l
    }, Nn = i => {
        const t = r.inject("editorId"), e = r.inject("previewOnly"), n = l => {
            if (l.clipboardData && l.clipboardData.files.length > 0) {
                const {files: o} = l.clipboardData;
                C.emit(t, "uploadImage", Array.from(o).filter(a => /image\/.*/.test(a.type))), l.preventDefault()
            }
        };
        r.onMounted(() => {
            e || i.value.addEventListener("paste", n)
        }), r.onBeforeUnmount(() => {
            e || i.value.removeEventListener("paste", n)
        })
    }, Tn = i => {
        const t = r.inject("editorId"), e = pe(() => {
            const n = document.querySelectorAll(`#${t}-preview img[zoom]`);
            n.length !== 0 && wn(n, {background: "#00000073"})
        });
        r.onMounted(e), r.watch([i], e)
    }, Vn = () => ({
        value: {type: String, default: ""},
        onChange: {
            type: Function, default: () => () => {
            }
        },
        setting: {type: Object, default: () => ({})},
        onHtmlChanged: {
            type: Function, default: () => () => {
            }
        },
        onGetCatalog: {
            type: Function, default: () => () => {
            }
        },
        markedHeadingId: {type: Function, default: () => ""},
        noMermaid: {type: Boolean, default: !1},
        sanitize: {type: Function, default: i => i},
        placeholder: {type: String, default: ""},
        noKatex: {type: Boolean, default: !1},
        scrollAuto: {type: Boolean}
    });
    var zn = r.defineComponent({
        name: "MDEditorContent", props: Vn(), setup(i) {
            const t = r.ref(!0), e = r.inject("previewOnly"), n = r.inject("showCodeRowNumber"),
                l = r.inject("previewTheme"), o = r.inject("editorId"), a = r.ref(), s = r.ref(), d = r.ref(),
                h = En(i), {html: u} = $n(i, h);
            return Cn(i, u, a, s, d), Sn(i, a), xn(i, a, t), Nn(a), Tn(u), () => r.createVNode(r.Fragment, null, [r.createVNode("div", {class: `${c}-content`}, [!e && r.createVNode("div", {class: `${c}-input-wrapper`}, [r.createVNode("textarea", {
                id: `${o}-textarea`,
                ref: a,
                value: i.value,
                onKeydown: () => {
                    C.emit(o, "saveHistoryPos", !0)
                },
                onCompositionstart: () => {
                    t.value = !1
                },
                onInput: g => {
                    i.onChange(g.target.value)
                },
                onCompositionend: () => {
                    t.value = !0
                },
                class: [i.setting.preview || i.setting.htmlPreview ? "" : "textarea-only"],
                placeholder: i.placeholder
            }, null)]), i.setting.preview && r.createVNode("div", {
                id: `${o}-preview-wrapper`,
                class: `${c}-preview-wrapper`,
                ref: s,
                key: "content-preview-wrapper"
            }, [r.createVNode("div", {
                id: `${o}-preview`,
                class: [`${c}-preview`, `${l == null ? void 0 : l.value}-theme`, n && `${c}-scrn`],
                innerHTML: u.value
            }, null)]), i.setting.htmlPreview && r.createVNode("div", {
                class: `${c}-preview-wrapper`,
                ref: d,
                key: "html-preview-wrapper"
            }, [r.createVNode("div", {class: `${c}-html`}, [u.value])])])])
        }
    }), An = r.defineComponent({
        props: {modelValue: {type: String, default: ""}}, setup(i) {
            const t = r.inject("usedLanguageText");
            return () => {
                var e;
                return r.createVNode("div", {class: `${c}-footer-item`}, [r.createVNode("label", {class: `${c}-footer-label`}, [(e = t.value.footer) == null ? void 0 : e.markdownTotal, r.createTextVNode(":")]), r.createVNode("span", null, [i.modelValue.length])])
            }
        }
    }), ti = "";
    const Ln = () => ({
        checked: {type: Boolean, default: !1}, onChange: {
            type: Function, default: () => () => {
            }
        }
    });
    var In = r.defineComponent({
        props: Ln(), setup(i) {
            return () => r.createVNode("div", {
                class: [`${c}-checkbox`, i.checked && `${c}-checkbox-checked`],
                onClick: () => {
                    i.onChange(!i.checked)
                }
            }, null)
        }
    });
    const Fn = () => ({
        scrollAuto: {type: Boolean}, onScrollAutoChange: {
            type: Function, default: () => () => {
            }
        }
    });
    var jn = r.defineComponent({
        props: Fn(), setup(i) {
            const t = r.inject("usedLanguageText");
            return () => {
                var e;
                return r.createVNode("div", {class: `${c}-footer-item`}, [r.createVNode("label", {
                    class: `${c}-footer-label`,
                    onClick: () => {
                        i.onScrollAutoChange(!i.scrollAuto)
                    }
                }, [(e = t.value.footer) == null ? void 0 : e.scrollAuto]), r.createVNode(In, {
                    checked: i.scrollAuto,
                    onChange: i.onScrollAutoChange
                }, null)])
            }
        }
    });
    const Dn = () => ({
        modelValue: {type: String, default: ""},
        footers: {type: Array, default: []},
        scrollAuto: {type: Boolean},
        onScrollAutoChange: {
            type: Function, default: () => () => {
            }
        },
        defFooters: {type: Object}
    });
    var _n = r.defineComponent({
        name: "MDEditorFooter", props: Dn(), setup(i) {
            const t = r.computed(() => {
                const n = i.footers.indexOf("="), l = n === -1 ? i.footers : i.footers.slice(0, n),
                    o = n === -1 ? [] : i.footers.slice(n, Number.MAX_SAFE_INTEGER);
                return [l, o]
            }), e = n => {
                if (qe.includes(n)) switch (n) {
                    case "markdownTotal":
                        return r.createVNode(An, {modelValue: i.modelValue}, null);
                    case "scrollSwitch":
                        return r.createVNode(jn, {
                            scrollAuto: i.scrollAuto,
                            onScrollAutoChange: i.onScrollAutoChange
                        }, null)
                } else return i.defFooters instanceof Array ? i.defFooters[n] || "" : i.defFooters && i.defFooters.children instanceof Array && i.defFooters.children[n] || ""
            };
            return () => {
                const n = t.value[0].map(o => e(o)), l = t.value[1].map(o => e(o));
                return r.createVNode("div", {class: `${c}-footer`}, [r.createVNode("div", {class: `${c}-footer-left`}, [n]), r.createVNode("div", {class: `${c}-footer-right`}, [l])])
            }
        }
    });
    const Rn = () => ({
        tocItem: {type: Object, default: () => ({})},
        markedHeadingId: {
            type: Function, default: () => () => {
            }
        },
        scrollElement: {type: [String, Object], default: ""},
        onClick: {
            type: Function, default: () => () => {
            }
        }
    }), mt = r.defineComponent({
        props: Rn(), setup({tocItem: i, markedHeadingId: t, scrollElement: e, onClick: n}) {
            return () => r.createVNode("div", {
                class: `${c}-catalog-link`, onClick: l => {
                    n(l, i), l.stopPropagation();
                    const o = t(i.text, i.level, i.index), a = document.getElementById(o),
                        s = e instanceof Element ? e : document.querySelector(e);
                    if (a && s) {
                        let d = a.offsetParent, h = a.offsetTop;
                        if (s.contains(d)) for (; d && s != d;) h += d == null ? void 0 : d.offsetTop, d = d == null ? void 0 : d.offsetParent;
                        s == null || s.scrollTo({top: h, behavior: "smooth"})
                    }
                }
            }, [r.createVNode("span", {title: i.text}, [i.text]), r.createVNode("div", {class: `${c}-catalog-wrapper`}, [i.children && i.children.map(l => r.createVNode(mt, {
                markedHeadingId: t,
                key: l.text,
                tocItem: l,
                scrollElement: e,
                onClick: n
            }, null))])])
        }
    });
    var ni = "";
    const Pn = () => ({
        editorId: {type: String},
        class: {type: String, default: ""},
        markedHeadingId: {type: Function, default: i => i},
        scrollElement: {type: [String, Object]},
        theme: {type: String, default: "light"}
    }), xe = r.defineComponent({
        name: "MdCatalog", props: Pn(), emits: ["onClick"], setup(i, t) {
            const e = i.editorId,
                n = r.reactive({list: [], show: !1, scrollElement: i.scrollElement || `#${e}-preview-wrapper`}),
                l = r.computed(() => {
                    const o = [];
                    return n.list.forEach(({text: a, level: s}, d) => {
                        const h = {level: s, text: a, index: d + 1};
                        if (o.length === 0) o.push(h); else {
                            let u = o[o.length - 1];
                            if (h.level > u.level) for (let g = u.level + 1; g <= 6; g++) {
                                const {children: k} = u;
                                if (!k) {
                                    u.children = [h];
                                    break
                                }
                                if (u = k[k.length - 1], h.level <= u.level) {
                                    k.push(h);
                                    break
                                }
                            } else o.push(h)
                        }
                    }), o
                });
            return r.onMounted(() => {
                C.on(e, {
                    name: "catalogChanged", callback: o => {
                        n.list = o
                    }
                }), C.emit(e, "pushCatalog")
            }), () => r.createVNode("div", {class: `${c}-catalog${i.theme === "dark" ? "-dark" : ""} ${i.class}`}, [l.value.map(o => r.createVNode(mt, {
                markedHeadingId: i.markedHeadingId,
                tocItem: o,
                key: o.text,
                scrollElement: n.scrollElement,
                onClick: (a, s) => {
                    t.emit("onClick", a, s)
                }
            }, null))])
        }
    });
    var ii = "", li = "";
    const Hn = () => ({
        modelValue: {type: String, default: ""},
        theme: {type: String, default: "light"},
        class: {type: String, default: ""},
        historyLength: {type: Number, default: 10},
        onChange: {type: Function},
        onSave: {type: Function},
        onUploadImg: {type: Function},
        pageFullScreen: {type: Boolean, default: !1},
        preview: {type: Boolean, default: !0},
        htmlPreview: {type: Boolean, default: !1},
        previewOnly: {type: Boolean, default: !1},
        language: {type: String, default: "zh-CN"},
        toolbars: {type: Array, default: Ue},
        toolbarsExclude: {type: Array, default: []},
        noPrettier: {type: Boolean, default: !1},
        onHtmlChanged: {type: Function},
        onGetCatalog: {type: Function},
        editorId: {type: String, default: "md-editor-v3"},
        tabWidth: {type: Number, default: 2},
        showCodeRowNumber: {type: Boolean, default: !1},
        previewTheme: {type: String, default: "default"},
        style: {type: Object, default: () => ({})},
        markedHeadingId: {type: Function, default: On},
        tableShape: {type: Array, default: () => [6, 4]},
        noMermaid: {type: Boolean, default: !1},
        sanitize: {type: Function, default: i => i},
        placeholder: {type: String, default: ""},
        noKatex: {type: Boolean, default: !1},
        defToolbars: {type: [String, Object]},
        onError: {type: Function},
        codeTheme: {type: String, default: "atom"},
        footers: {type: Array, default: qe},
        scrollAuto: {type: Boolean, default: !0},
        defFooters: {type: [String, Object]},
        noIconfont: {type: Boolean}
    }), On = i => i, ne = r.defineComponent({
        name: "MdEditorV3",
        props: Hn(),
        emits: ["onChange", "onSave", "onUploadImg", "onHtmlChanged", "onGetCatalog", "onError", "update:modelValue"],
        setup(i, t) {
            const {editorId: e} = i, n = r.reactive({scrollAuto: i.scrollAuto});
            Et(i, t), Nt(i), Tt(i);
            const [l, o] = Vt(i, t), [a, s] = zt(i);
            return r.onBeforeUnmount(() => {
                C.clear(e)
            }), () => {
                var u;
                const d = Q({props: i, ctx: t}, "defToolbars"), h = Q({props: i, ctx: t}, "defFooters");
                return r.createVNode("div", {
                    id: e,
                    class: [c, i.class, i.theme === "dark" && `${c}-dark`, l.fullscreen || l.pageFullScreen ? `${c}-fullscreen` : "", i.previewOnly && `${c}-previewOnly`],
                    style: i.style
                }, [!i.previewOnly && r.createVNode(Ut, {
                    noPrettier: i.noPrettier,
                    toolbars: i.toolbars,
                    toolbarsExclude: i.toolbarsExclude,
                    setting: l,
                    updateSetting: o,
                    tableShape: i.tableShape,
                    defToolbars: d
                }, null), r.createVNode(zn, {
                    value: i.modelValue,
                    onChange: g => {
                        C.emit(e, "saveHistoryPos"), i.onChange ? i.onChange(g) : (t.emit("update:modelValue", g), t.emit("onChange", g))
                    },
                    setting: l,
                    onHtmlChanged: g => {
                        i.onHtmlChanged ? i.onHtmlChanged(g) : t.emit("onHtmlChanged", g)
                    },
                    onGetCatalog: g => {
                        i.onGetCatalog ? i.onGetCatalog(g) : t.emit("onGetCatalog", g)
                    },
                    markedHeadingId: i.markedHeadingId,
                    noMermaid: i.noMermaid,
                    sanitize: i.sanitize,
                    placeholder: i.placeholder,
                    noKatex: i.noKatex,
                    scrollAuto: n.scrollAuto
                }, null), !i.previewOnly && ((u = i.footers) == null ? void 0 : u.length) > 0 && r.createVNode(_n, {
                    modelValue: i.modelValue,
                    footers: i.footers,
                    defFooters: h,
                    scrollAuto: n.scrollAuto,
                    onScrollAutoChange: g => n.scrollAuto = g
                }, null), s.value && r.createVNode(xe, {
                    theme: i.theme,
                    style: {display: a.value ? "block" : "none"},
                    class: `${c}-catalog-editor`,
                    editorId: e,
                    markedHeadingId: i.markedHeadingId
                }, null)])
            }
        }
    }), Mn = () => ({title: {type: String, default: ""}, trigger: {type: [String, Object]}, onClick: {type: Function}});
    var Re = r.defineComponent({
        name: "NormalToolbar", props: Mn(), emits: ["onClick"], setup(i, t) {
            return () => {
                const e = Q({props: i, ctx: t}, "trigger");
                return r.createVNode("div", {
                    class: `${c}-toolbar-item`, title: i.title, onClick: n => {
                        i.onClick instanceof Function ? i.onClick(n) : t.emit("onClick", n)
                    }
                }, [e])
            }
        }
    });
    const Bn = () => ({
        title: {type: String, default: ""},
        visible: {type: Boolean},
        trigger: {type: [String, Object]},
        onChange: {type: Function},
        overlay: {type: [String, Object]}
    });
    var Pe = r.defineComponent({
        name: "DropdownToolbar", props: Bn(), emits: ["onChange"], setup(i, t) {
            const e = r.inject("editorId");
            return () => {
                const n = Q({props: i, ctx: t}, "trigger"), l = Q({props: i, ctx: t}, "overlay");
                return r.createVNode(oe, {
                    relative: `#${e}-toolbar-wrapper`, visible: i.visible, onChange: o => {
                        i.onChange instanceof Function ? i.onChange(o) : t.emit("onChange", o)
                    }, overlay: l
                }, {default: () => [r.createVNode("div", {class: `${c}-toolbar-item`, title: i.title || ""}, [n])]})
            }
        }
    });

    function Un(i) {
        return typeof i == "function" || Object.prototype.toString.call(i) === "[object Object]" && !r.isVNode(i)
    }

    const qn = () => ({
        title: {type: String, default: ""},
        modalTitle: {type: String, default: ""},
        visible: {type: Boolean},
        width: {type: String, default: "auto"},
        height: {type: String, default: "auto"},
        trigger: {type: [String, Object]},
        onClick: {type: Function},
        onClose: {type: Function},
        showAdjust: {type: Boolean, default: !1},
        isFullscreen: {type: Boolean, default: !1},
        onAdjust: {type: Function}
    });
    var He = r.defineComponent({
        name: "ModalToolbar",
        props: qn(),
        emits: ["onClick", "onClose", "onAdjust"],
        setup(i, t) {
            return () => {
                const e = Q({props: i, ctx: t}, "trigger"), n = Q({props: i, ctx: t}, "default");
                return r.createVNode(r.Fragment, null, [r.createVNode("div", {
                    class: `${c}-toolbar-item`,
                    title: i.title,
                    onClick: () => {
                        i.onClick instanceof Function ? i.onClick() : t.emit("onClick")
                    }
                }, [e]), r.createVNode(je, {
                    width: i.width,
                    height: i.height,
                    title: i.modalTitle,
                    visible: i.visible,
                    onClose: () => {
                        i.onClose instanceof Function ? i.onClose() : t.emit("onClose")
                    },
                    showAdjust: i.showAdjust,
                    isFullscreen: i.isFullscreen,
                    onAdjust: l => {
                        i.onAdjust instanceof Function ? i.onAdjust(l) : t.emit("onAdjust", l)
                    }
                }, Un(n) ? n : {default: () => [n]})])
            }
        }
    });
    return ne.install = i => (i.component(ne.name, ne), i.component(Re.name, Re), i.component(Pe.name, Pe), i.component(xe.name, xe), i.component(He.name, He), i), ne.NormalToolbar = Re, ne.DropdownToolbar = Pe, ne.MdCatalog = xe, ne.ModalToolbar = He, ne.config = wt, ne
});