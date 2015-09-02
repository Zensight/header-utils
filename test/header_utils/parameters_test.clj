(ns header-utils.parameters-test
  (:require [header-utils.parameters :as p]
            [clojure.test :refer :all]))

(deftest test-encode
  (are [x y] (= (apply p/encode x) y)
    ["name" "value"]                "name=value"
    ["name*" "value"]               "name=value"
    ["name" "value" nil]            "name=value"
    ["name" "[quoted]"]             "name=\"[quoted]\""
    ["name" "quoted for spaces"]    "name=\"quoted for spaces\""
    ["name" "internal \"quotes\""]  "name=\"internal \\\"quotes\\\"\""
    ["name" "value" "language"]     "name*=UTF-8'language'value"
    ["name" "special҉"]            "name*=UTF-8''special%D2%89"
    ["name" "special҉" "language"] "name*=UTF-8'language'special%D2%89"))

(deftest parse
  (are [x y] (= (apply p/parse x) y)
    ["name=value"]                           {"name" "value"}
    ["name=\"[quoted]\""]                    {"name" "[quoted]"}
    ["name=\"quoted for spaces\""]           {"name" "quoted for spaces"}
    ["name=\"internal \\\"quotes\\\"\""]     {"name" "internal \"quotes\""}
    ["name*=UTF-8'language'value"]           {"name*" "value"}
    ["name*=UTF-8''special%D2%89"]           {"name*" "special҉"}
    ["name*=UTF-8'language'special%D2%89"]   {"name*" "special҉"}
    ["name*=ISO-8859-1'language'special%E7"] {"name*" "specialç"}))

(deftest find-parameter
  (are [x y] (= (apply p/find-parameter x) y)
    [{"other" "not-value"} "name"]                                nil
    [{"name" "value" "other" "not-value"} "name"]                 "value"
    [{"name" "value" "other" "not-value" "name*" "value"} "name"] "value"
    [{"name*" "value" "other" "not-value" "name" "value"} "name"] "value"))
