Pipeline{
    agent any
    Environment{
        APP_NAME = 'python - app'
        IMAGE_TAG = "${BUILD NUMBER}"
        DOCKER_IMAGE = "yourdockerhubusername/Python - app:${MY DOCKER_IMAGE}"
        KUBE_DEPLOYMENT = "my-app-deployment"
    }
    stages{
        Stage ('Clone rep'){
            steps{
                git 'https://github.com/Maniwq/Work.git'
            }
        }
        stage('Run Tests'){
             steps{
             sh 'pyth tests/'
             }
        }
        stage('build Docker image'){
            steps{
               sh " Docker build -t ${Docker Image}"
            }
        }
        stage('push DOCKER_IMAGE'){
            Steps{
            withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'Mani01', passwordVariable: 'Jenkins')]) {
                sh """
                    echo $Jenkins | docker login -u $Mani01 --jenkins-stdin
                    docker push ${DOCKER_IMAGE}
                    """
            }
        }
        stage('Deploy to kubernetes'){
            steps{
                sh "kubectl set image deployment/${APP_NAME} ${APP_NAME}=${DOCKER_IMAGE} --record"
            }
        }
    }
    post {
    sucess{
        echo 'python CI/CD Pipeline completed'
            }
    failure{
        echo 'python CI/CD Pipeline Failed'
            }
        }
    }
}
