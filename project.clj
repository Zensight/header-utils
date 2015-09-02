(defproject co.zensight/header-utils "0.1.0"
  :description "Tools for working HTTP headers"
  :url "http://github.com/zensight/header-utils"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/mit-license.html"}
  :deploy-repositories [["releases" :clojars]]
  :signing {:gpg-key "isaac@isaaccambron.com"}
  :scm {:name "git"
        :url "https://github.com/Zensight/file-buffer"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [instaparse "1.4.1"]])
