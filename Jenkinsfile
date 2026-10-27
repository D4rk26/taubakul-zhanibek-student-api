// Jenkinsfile — CI pipeline of taubakul-zhanibek-student-api (author: Taubakul Zhanibek)
// Declarative pipeline: Checkout -> Build -> Test -> Docker Build -> Verify.
// If a stage fails, the build is marked FAILURE and the following stages are skipped.

pipeline {
    // Run on a node with the "docker" label (the built-in node has Docker access)
    agent { label 'docker' }

    // JDK 21 configured in Jenkins (jenkins/casc.yaml): sets JAVA_HOME and PATH
    tools {
        jdk 'jdk-21'
    }

    options {
        timestamps()                                    // time for every console line
        timeout(time: 30, unit: 'MINUTES')              // abort a hanging build
        buildDiscarder(logRotator(numToKeepStr: '20'))  // keep the last 20 builds
        disableConcurrentBuilds()                       // one build per branch at a time
    }

    environment {
        STUDENT_NAME = 'Taubakul Zhanibek Nurlanuly'
        IMAGE_NAME   = 'taubakul-zhanibek-student-api'
        IMAGE_TAG    = 'v1.0-taubakul-zhanibek'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    currentBuild.displayName = "#${env.BUILD_NUMBER} Taubakul Zhanibek"
                }
                // Get the source code of this branch from GitHub
                checkout scm
                echo "Pipeline of ${env.STUDENT_NAME}: branch ${env.BRANCH_NAME}, commit ${env.GIT_COMMIT}"
                sh 'git log -1 --oneline'
            }
        }

        stage('Build') {
            steps {
                // Compile and package the application without running tests
                sh './gradlew clean assemble --no-daemon'
            }
        }

        stage('Test') {
            steps {
                // A failing test makes this step exit with a non-zero code -> stage fails
                sh './gradlew test --no-daemon'
            }
            post {
                always {
                    // Publish JUnit results even when tests fail
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                // Build the personalized image; the second tag identifies this build
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:build-${BUILD_NUMBER} .'
            }
        }

        stage('Verify') {
            steps {
                // Fails the build if the image does not exist
                sh '''
                    if docker image inspect "${IMAGE_NAME}:${IMAGE_TAG}" > /dev/null 2>&1; then
                        docker image inspect "${IMAGE_NAME}:${IMAGE_TAG}" --format 'ID: {{.Id}}
Created: {{.Created}}
Size: {{.Size}} bytes
Author label: {{index .Config.Labels "author"}}'
                        echo "Image ${IMAGE_NAME}:${IMAGE_TAG} exists — VERIFIED by Taubakul Zhanibek"
                    else
                        echo "ERROR: image ${IMAGE_NAME}:${IMAGE_TAG} was not found"
                        exit 1
                    fi
                '''
            }
        }
    }

    post {
        success {
            echo "SUCCESS: build ${env.BUILD_NUMBER} of ${env.STUDENT_NAME} passed all stages"
        }
        failure {
            echo "FAILURE: build ${env.BUILD_NUMBER} of ${env.STUDENT_NAME} failed — see the failed stage above"
        }
        always {
            echo "Pipeline finished for ${env.STUDENT_NAME} (result: ${currentBuild.currentResult})"
            // Delete the workspace to save disk space
            cleanWs()
        }
    }
}
