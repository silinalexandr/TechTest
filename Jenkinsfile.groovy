// Jenkinsfile

@Library('shared_library') _

import main.groovy.ru.sbrf.example.shared_library.Pipeline
import main.groovy.ru.sbrf.example.shared_library.Stage

class HelloWorldStage extends Stage {

    HelloWorldStage(String name) { super(name) }

    @Override
    protected void execute(Script script) {
        logging(script, 'Hello world')
    }
}

Pipeline pipeline = new Pipeline(
    this,
    [
        new HelloWorldStage('Hello world')
    ]
)
pipeline.execute()

pipeline {
    agent any

    stages {
        stage('Execute Shared Pipeline') {
            steps {
                script {
                    executePipeline()
                }
            }
        }

    
 stages {
        stage('Clone Repository') {
            steps {
                script {
                    checkout([$class: 'GitSCM', branches: [[name: 'main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'your-repo-url']]])
                }
            }
        }

        stage('Restore Helm Chart Dependencies') {
            steps {
                script {
                    sh 'helm dependency build charts/hello-world'
                }
            }
        }

        stage('Archive Helm Charts') {
            steps {
                script {
                    sh 'tar czf helm-charts.tar.gz helm-charts'
                }
            }
        }
    }
   

    }

    post {
        always {
            script {
                // Clean up after the pipeline
                deleteDir()
            }
        }
    }
}
