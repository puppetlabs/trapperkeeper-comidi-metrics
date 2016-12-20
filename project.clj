(defproject puppetlabs/trapperkeeper-comidi-metrics "0.1.3-SNAPSHOT"
  :description "Comidi/HTTP Metrics for Trapperkeeper"
  :url "http://github.com/puppetlabs/trapperkeeper-comidi-metrics"

  :min-lein-version "2.7.1"

  :pedantic? :abort

  :parent-project {:coords [puppetlabs/clj-parent "0.3.0"]
                   :inherit [:managed-dependencies]}

  :dependencies [[org.clojure/clojure]
                 [prismatic/schema]
                 [puppetlabs/trapperkeeper-metrics]
                 [puppetlabs/comidi]]

  :plugins [[lein-parent "0.3.1"]]

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/clojars_jenkins_username
                                     :password :env/clojars_jenkins_password
                                     :sign-releases false}]]

  :profiles {:dev {:source-paths ["dev"]
                   :dependencies [[puppetlabs/trapperkeeper :classifier "test"]
                                  [puppetlabs/kitchensink :classifier "test"]
                                  [puppetlabs/trapperkeeper-status]
                                  [puppetlabs/http-client]
                                  [puppetlabs/trapperkeeper-webserver-jetty9]]}}

  :aliases {"example" ["run" "-m" "example.comidi-metrics-web-app"]
            "example-data" ["run" "-m" "example.traffic-generator"]})
