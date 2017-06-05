![WTFPL](http://www.wtfpl.net/wp-content/uploads/2012/12/wtfpl-badge-4.png)
[WTFPL](http://www.wtfpl.net)

# Klusterfuck
Klusterfuck is a serverless framework for kubernetes. Serverless, or Functions-as-a-Service (FaaS)
is a model where a software developer is only concerned about small stateless functions and the platform
manages everything else, like deployments, ingress/egress, load-balancing, scaling...

## How it works
Klusterfuck works on kubernetes and kubernetes only. 
It uses a gogs (go git service) server to
create git repositories for storing and versioning the code for functions. When creating a function
with klusterfuck, a git repository is created with a default configuration. Developers then clone the repo 
and push changes to it. Klusteruck admin REST API or the included dashboard can be used to update or rollback to 
a specific commit id in the git repo.

For every function, a kubernetes deployment and service are created. These services run a worker image which is 
responsible for checking out the currently selected commit id from git and reading configuration and
function code from it.

### how to use:
Klusterfuck is not meant for serious use-cases yet, please only try it in a secure
network using minikube or an isolated kubernetes deployment.

As of now, there is no security and admin and agent APIs are open to all. 

This project contains a kubernetes manifest file which will create the klusterfuck admin 
deployment and service and a gogs deployment and service. Note that both services are of ClusterIP type, 
so use port-forwarding to try them. When creating functions, it is possible to create different kinds of 
services for exposing functions over HTTP: ClusterIP and NodePort are supported. Additinoally, when creating 
a function, it is also possible to specify an Ingress resource for the function.

#### Installation
To deploy, for kubernetes version < 1.6
 run kubectl create -f https://raw.githubusercontent.com/n-k/klusterfuck/master/k8s/all_k8s_15.yaml

For kubernetes version >= 1.6,
    run kubectl create -f https://raw.githubusercontent.com/n-k/klusterfuck/master/k8s/all.yaml

This will create the deployment and services in 'klusterfuck' namespace.

Wait for the pods to start and reach 'running' state. Then port forward the klusterfuck-admin pod by running:

kubectl -n klusterfuck port-forward $(kubectl -n klusterfuck get pod -l app=klusterfuck -o template --template="{{(index .items 0).metadata.name}}") 8080:8080

Now you should be able to go to localhost:8080 in your favorite browser and reach the klusterfuck console.

### Dev docs?
Will add if anyone asks.

## License
Everything in this repo is distributed under the WTF public license (WTFPL). A copy of the license can be found in project root.

