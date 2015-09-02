(ns header-utils.encoding-test
  (:require [header-utils.encoding :as e]
            [clojure.test :refer :all]))

(deftest test-quote
  (is (= (e/quote-str "fine") "fine"))
  (is (= (e/quote-str "stuff < things") "\"stuff < things\""))
  (is (= (e/quote-str "\"") "\"\\\"\""))
  (is (= (e/quote-str "\\hello") "\"\\\\hello\""))
  (is (= (e/quote-str "this is \\a\\ crappy [\"string\"]") "\"this is \\\\a\\\\ crappy [\\\"string\\\"]\"")))

(deftest test-percent-decode
  (is (= (e/percent-decode "fine" "UTF-8") "fine"))
  (is (= (e/percent-decode "this%20has%20spaces" "UTF-8") "this has spaces"))
  (is (= (e/percent-decode "I have 95%25 test coverage" "UTF-8") "I have 95% test coverage")))

(deftest test-percent-encode
  (is (= (e/percent-encode "fine" "UTF-8") "fine"))
  (is (= (e/percent-encode "this has spaces" "UTF-8") "this%20has%20spaces"))
  (is (= (e/percent-encode "plus+" "UTF-8") "plus%2B"))
  (is (= (e/percent-encode "special҉" "UTF-8") "special%D2%89")))

(deftest test-all-ascii?
  (is (e/all-ascii? "hello, how are you doing?"))
  (is (not (e/all-ascii? "I b́e̡ckoǹ."))))
