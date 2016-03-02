(defproject mrmcc3/frontend-build-components "0.1.1-SNAPSHOT"
  :description "Components for building front-end projects with clojure"
  :url "https://github.com/mrmcc3/frontend-build-components"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["clojars" {:sign-releases false}]]
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.7.228" :scope "provided"]
                 [com.stuartsierra/component "0.3.1"]
                 [figwheel-sidecar "0.5.0-6"]
                 [mrmcc3/libsass-clj "0.1.4"]])
