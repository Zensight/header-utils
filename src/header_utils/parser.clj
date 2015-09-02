(ns header-utils.parser
  "Parsing utilities for header-utils. Probably only useful in extending the library."
  (:require [instaparse.core :as insta]))

(def ^:private parser (insta/parser (clojure.java.io/resource "grammar.txt") :input-format :abnf))

;;utilities for xforming the parsing results

(defn tagged
  "Returns a function that takes an instaparse node and returns true if the node has name `tag`."
  [tag]
  #(#{tag} (first %)))

(defn value-in-tag
  "Shortcut for finding the first value in the first node with a given tag."
  [tag list] (-> tag tagged (filter list) first second))

(def ^:private xform-2616
  {:token str
   :token-char str
   :quoted-pair (constantly "\"")
   :qdtext str
   :quoted-string str})

(defn parse*
  "Given a string and a starting rule from the grammar, parse the string and return the raw instaparse tree. Useful for debugging."
  [value start]
  (binding [instaparse.abnf/*case-insensitive* true]
    (parser value :start start)))

(defn parse
  "Given a string, a starting rule from the grammar, and a transformation map, return a parsed structure. Useful for extending this library."
  [value start xform]
  (->>
   (parse* value start)
   (insta/transform (merge xform xform-2616))))

