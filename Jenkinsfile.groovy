// Jenkinsfile

@Library('shared_library') _

pipeline {
    agent any

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

    post {
        always {
            script {
                // Clean up after the pipeline
                deleteDir()
            }
        }
    }
}
