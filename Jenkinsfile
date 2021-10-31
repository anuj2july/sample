#!/usr/bin/env groovy

def mvnHome = '/var/lib/jenkins/tools/hudson.tasks.Maven_MavenInstallation/Maven.3.0.5'
def cucumberHtmlReportPath = 'target/cucumber-reports'

pipeline {

    agent any

    options {
        ansiColor('xterm')
    }
    parameters {
        choice(
                name: 'TAGS',
                choices: ['All','@PortalOnlineGovPay','@PortalOnlineGovPayCardFailure','@AgentAssistPayment','@IVRpayment'],
                description: 'Choose a tag to run the corresponding tests'
        )

        choice(
                name: 'environment',
                choices: ['dt','di','dp'],
                description: 'Choose the target environment'
        )
        choice(
                name: 'browser',
                choices: [ 'htmlunit'],
                description: 'Choose the browser'
        )
    }
    stages {
        stage('Prepare Workspace and checkout') {
            steps {
                cleanWs()
                checkout scm
//                script {
//                    jwt_sigining_key_1 = sh (
//                            script: "aws ssm get-parameter --name /sit/cps-card-payments-service/JWT_SIGNING_KEY --output text --query Parameter.Value --region eu-west-2",
//                            returnStdout: true
//                    ).trim()
//                    env.jwt_sigining_key = "${jwt_sigining_key_1}"
//                    echo "jwt_sigining_key: ${env.jwt_sigining_key}"
//                }
//                script {
//                    token_issuer_1 = sh (
//                            script: "aws ssm get-parameter --name /sit/cps-card-payments-service/TOKEN_ISSUER --output text --query Parameter.Value --region eu-west-2",
//                            returnStdout: true
//                    ).trim()
//                    env.token_issuer = "${token_issuer_1}"
//                    echo "token_issuer: ${env.token_issuer}"
//                }
            }
        }

        stage('Build') {
            steps {
                sh "'${mvnHome}/bin/mvn' clean compile"
            }
        }

        stage('Run Tests and Generate reports') {
            steps {

                script {
                    if ("${params.TAGS}" == 'All') {
                        sh "'${mvnHome}/bin/mvn' verify -P ${params.environment}"
                    } else {
                        sh "'${mvnHome}/bin/mvn' verify -P ${params.environment} -Dcucumber.options='--tags ${params.TAGS}'"
                    }
                }
            }
        }
    }

    post {
        always {
            publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: "${cucumberHtmlReportPath}",
                    reportFiles: 'report.html',
                    reportName: 'AutomationTestReport',
                    reportTitles: 'CPS Automation-test-reports'
            ])
        }
    }
}
