(defproject com.taoensso/encore "2.115.0"
  :author "Peter Taoussanis <https://www.taoensso.com>"
  :description "Core utils library for Clojure/Script"
  :url "https://github.com/ptaoussanis/encore"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "Same as Clojure"}
  :min-lein-version "2.3.3"
  :global-vars {*warn-on-reflection* true
                *assert*             true
                ;; *unchecked-math*  :warn-on-boxed
                }

  :dependencies
  [[org.clojure/clojure      "1.5.1"]
   [org.clojure/tools.reader "1.3.2"]
   [com.taoensso/truss       "1.5.0"]]

  :plugins
  [[lein-pprint  "1.2.0"]
   [lein-ancient "0.6.15"]
   [lein-codox   "0.10.7"]]

  :profiles
  {;; :default [:base :system :user :provided :dev]
   :server-jvm {:jvm-opts ^:replace ["-server"]}
   :1.5  {:dependencies [[org.clojure/clojure "1.5.1"]]}
   :1.6  {:dependencies [[org.clojure/clojure "1.6.0"]]}
   :1.7  {:dependencies [[org.clojure/clojure "1.7.0"]]}
   :1.8  {:dependencies [[org.clojure/clojure "1.8.0"]]}
   :1.9  {:dependencies [[org.clojure/clojure "1.9.0"]]}
   :1.10 {:dependencies [[org.clojure/clojure "1.10.1"]]}
   :test {:dependencies [[org.clojure/test.check "0.9.0"]]}
   :dev
   [:1.10 :test :server-jvm
    {:dependencies
     [[org.clojure/clojurescript "1.10.520"]
      [org.clojure/core.async    "0.4.500"]]

     :plugins
     [;; These must be in :dev, Ref. https://github.com/lynaghk/cljx/issues/47:
      [com.keminglabs/cljx "0.6.0"]
      [lein-cljsbuild      "1.1.7"]]}]}

  ;; :jar-exclusions [#"\.cljx|\.DS_Store"]

  :source-paths ["src" "target/classes"]
  :test-paths   ["src" "test" "target/test-classes"]

  :cljx
  {:builds
   [{:source-paths ["src"]        :rules :clj  :output-path "target/classes"}
    {:source-paths ["src"]        :rules :cljs :output-path "target/classes"}
    {:source-paths ["src" "test"] :rules :clj  :output-path "target/test-classes"}
    {:source-paths ["src" "test"] :rules :cljs :output-path "target/test-classes"}]}

  :cljsbuild
  {:test-commands {}
   :builds
   [{:id "main"
     :source-paths   ["src" "target/classes"]
     ;; :notify-command ["terminal-notifier" "-title" "cljsbuild" "-message"]
     :compiler       {:output-to "target/main.js"
                      :optimizations :advanced
                      :pretty-print false}}
    {:id "tests"
     :source-paths   ["src" "target/classes" "test" "target/test-classes"]
     ;; :notify-command []
     :compiler       {:output-to "target/tests.js"
                      :optimizations :whitespace
                      :pretty-print true
                      :main "taoensso.encore.tests"}}]}

  :auto-clean false
  :prep-tasks [["cljx" "once"] "javac" "compile"]

  :codox
  {:language :clojure ; [:clojure :clojurescript] ; No support?
   :source-paths ["target/classes"]
   :source-uri
   {#"target/classes" "https://github.com/ptaoussanis/encore/blob/master/src/{classpath}x#L{line}"
    #".*"             "https://github.com/ptaoussanis/encore/blob/master/{filepath}#L{line}"}}

  :aliases
  {"test-all"   ["do" "clean," "cljx" "once,"
                 "with-profile" "+1.10:+1.9:+1.8:+1.7:+1.6:+1.5" "test,"
                 ;; "with-profile" "+test" "cljsbuild" "test"
                 ]
   "build-once" ["do" "clean," "cljx" "once," "cljsbuild" "once" "main"]
   "deploy-lib" ["do" "build-once," "deploy" "clojars," "install"]
   "start-dev"  ["with-profile" "+dev" "repl" ":headless"]}

  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"})
