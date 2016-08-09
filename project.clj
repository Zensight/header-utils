(defproject co.zensight/header-utils "0.2.2"
  :description "Tools for working HTTP headers"
  :url "http://github.com/zensight/header-utils"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/mit-license.html"}
  :deploy-repositories [["releases" :clojars]]
  :signing {:gpg-key "748C1352"}
  :scm {:name "git"
        :url "https://github.com/Zensight/header-utils"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [instaparse "1.4.1"]])
