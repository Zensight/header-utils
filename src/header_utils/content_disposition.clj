(ns header-utils.content-disposition
  "Tools for encoding and parsing Content-Disposition headers according to RFC 6266."
  (:require [clojure.string :as str]
            [header-utils.parameters :as parm]
            [header-utils.parser :as p]))

(def header-name "Content-Disposition")

(def ^:private xform-6266
  {:content-disposition (fn [& children]
                          (reduce (fn [result [name & children]]
                                    (condp = name
                                      :content-disposition-type (assoc result :type (str/lower-case (first children)))
                                      :content-disposition-param (update-in result [:parameters] merge (first children))
                                      result))
                                  {} children))})

(defn- parse [value]
  (if (empty? value)
    nil
    (->>
      (p/parse value :content-disposition (merge xform-6266 parm/xform-5987))
      (merge {:parameters []}))))

(defn- disposition-type [parsed]
  (when parsed
    (:type parsed)))

(defn- parameter [parsed name]
  (when parsed
    (parm/find-parameter (:parameters parsed) name)))

(defn- filename [parsed]
  (parameter parsed "filename"))

(def parse-type
  "Retrieve the disposition type from the Content-Disposition value. Typically \"inline\" or \"attachment\"."
  (comp disposition-type parse))

(def parse-filename
  "Retrieve the (decoded) filename from the Content-Disposition value. Prefers extended values to regular ones if both are present. May be nil."
  (comp filename parse))

(defn parse-parameter
  "Retrieve an arbitrary parameter from the Content-Disposition value."
  [value param-name]
  (-> value parse (parameter param-name)))

(defn encode
  "Write the value of the Content-Disposition header (i.e. just the right-hand side) for a type, filename, an option language (e.g. \"en\"), and an optional map of other parameters. Filename may be nil."
  ([type filename] (encode type filename nil {}))
  ([type filename language more]
    (let [parameters (if filename (merge more {:filename filename}) more)]
      (->>
        (conj (map
               (fn [[k v]] (parm/encode (name k) v language)) parameters)
              type)
        (str/join ";")))))
