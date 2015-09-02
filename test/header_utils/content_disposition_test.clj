(ns header-utils.content-disposition-test
  (:require [header-utils.content-disposition :as cd]
            [clojure.test :refer :all]))

(deftest filename-test
  (are [x y] (= (cd/parse-filename x) y)
    "Attachment; filename=example.html" "example.html"
    "attachment;\r\n filename*= UTF-8''%e2%82%ac%20rates" "€ rates"
    "attachment; filename=\"EURO rates\"; filename*=utf-8''%e2%82%ac%20rates" "€ rates"
    "attachment;filename=\"this has spaces.pdf\"" "this has spaces.pdf"
    "inline" nil
    "asdfas:e҉rasdfasfe;a*^*&F" nil
    "attachment:filename=improperly spaced" nil
    "" nil
    nil nil))

(deftest disposition-type-test
  (are [x y] (= (cd/parse-type x) y)
    "Attachment; filename=example.html" "attachment"
    "inline" "inline"
    "INLINE" "inline"
    "" nil
    nil nil))

(deftest parameter-test
  (are [x y] (= (cd/parse-parameter x "random") y)
    "Attachment;random=cheese" "cheese"
    "inline;filename=dude; random=goats" "goats"
    "inline;random*=UTF-8''special%D2%89" "special҉"))

(deftest encode-test
  (are [x y] (= (apply cd/encode x) y)
    ["inline" nil]                                  "inline"
    ["inline" "foo.txt"]                            "inline;filename=foo.txt"
    ["inline" "foo bar.txt"]                        "inline;filename=\"foo bar.txt\""
    ["inline" "foo \" bar.txt"]                     "inline;filename=\"foo \\\" bar.txt\""
    ["inline" "special҉"]                          "inline;filename*=UTF-8''special%D2%89"
    ["inline" "need-language" "en" {}]              "inline;filename*=UTF-8'en'need-language"
    ["inline" "more-params" nil {:foo "bar"}]       "inline;filename=more-params;foo=bar"
    ["inline" "more-params" nil {:foo "special҉"}] "inline;filename=more-params;foo*=UTF-8''special%D2%89"))
