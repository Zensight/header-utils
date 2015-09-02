(ns header-utils.encoding
  "Tools for encoding and decoding strings in http headers."
  (:require [clojure.string :as s]
            [clojure.set :as se])
  (:import [java.net URLDecoder URLEncoder]))

;;Some of this is redundant with the grammar in the parser. That's a bummer, but it's hard to fix without inlining
;;the grammar itself, which is a pain because Clojure lacks heredocs.
(def separator-chars #{\( \) \< \> \@ \, \; \: \\ \" \/ \[ \] \? \= \{ \} \space \tab})
(def non-attr-chars (se/union #{\* \' \%} separator-chars))

(defn- normalize-charset [charset]
  (s/upper-case charset))

(defn- ascii? [c]
  (< 31 (int c) 127))

(defn- attr-char? [c]
  (and (ascii? c)
       (not (non-attr-chars c))))

(defn quote-str
  "Quote if needed, otherwise leave as-is."
  [value]
  (if (some separator-chars value)
    (as->
        value
        $
        (s/replace $ #"\\" "\\\\\\\\") ;;yes, 8 fucking backslashes
        (s/replace $ #"\"" "\\\\\"")
        (str "\"" $ "\""))
    value))

(defn percent-decode
  "Decode %HEX HEX to the appropriate encoding."
  [value encoding]
  (URLDecoder/decode value (normalize-charset encoding)))

(defn percent-encode
  "Encode with %HEX HEX for values outside of allowed attribute values."
  [value encoding]
  (as->
   (for [c value]
     (if (and (attr-char? c) (not= \+ c)) ;;we're cheating here so that we can use URLEncoder, which replaces spaces with +
       c
       (URLEncoder/encode (str c) (normalize-charset encoding))))
    $
    (apply str $)
    (s/replace $ #"\+" "%20")))

(defn all-ascii? [value]
  (every? ascii? value))
