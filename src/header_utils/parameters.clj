(ns header-utils.parameters
  "Tools for encoding and parsing http header parameters according to RFC 5987."
  (:require [clojure.string :as str]
            [header-utils.encoding :as e]
            [header-utils.parser :as p]))

(def xform-5987
  {:parameter identity
   :reg-parameter (fn [name & others] {name (p/value-in-tag :reg-value others)})
   :ext-parameter (fn [name & others] {(str name "*") (p/value-in-tag :ext-value others)})
   :ext-value (fn [& items]
                (let [charset (p/value-in-tag :charset items)
                      value-chars (p/value-in-tag :ext-value-chars items)]
                  [:ext-value (e/percent-decode value-chars (str/upper-case charset))]))
   :ext-value-chars (fn [& s] [:ext-value-chars (apply str s)])
   :parmname str
   :mime-charsetc str
   :mime-charset str
   :attr-char str
   :pct-encoded str
   :HEXDIG str
   :ALPHA str
   :DIGIT str})

(defn encode
  "Encodes the name, value, and optionally language as an RFC-5897 header, handling all encoding for you. Returns a string that looks like
   name=value or name=quoted-value or name*=encoded-value, depending on the contents of the string and the options provided."
  ([name value] (encode name value nil))
  ([name value language]
   (let [simple-name (.replace name "*" "")]
     ;;I'm *super* unclear on whether ISO-8859-1 chars outside of US-ASCII are allowed in tokens or quoted strings.
     ;;2616 implies they aren't but 5987 implies they are. We're going to assume they're not.
     (if (or language (not (e/all-ascii? value)))
       (str simple-name "*=" (str "UTF-8'" language "'" (e/percent-encode value "UTF-8")))
       (str simple-name "=" (e/quote-str value))))))

(defn parse
  "Parse a parameter, e.g. `(parse \"name=value\")`. Handles encodings transparently. Currently discards language."
  [string]
  (p/parse string :parameter xform-5987))

(defn find-parameter
  "Find a value by `param-name` in a `param-map` (such as the one produced by calling `parse` and merging the results). Prefers extended versions of the parameters. Useful for eliding the difference between [param]* and [param]."
  [param-map param-name]
  (if-let [starred (get param-map (str param-name "*"))]
    starred
    (get param-map param-name)))
