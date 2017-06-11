![WTFPL](http://www.wtfpl.net/wp-content/uploads/2012/12/wtfpl-badge-4.png)
[WTFPL](http://www.wtfpl.net)

# Klusterfuck
Klusterfuck is a serverless framework for kubernetes. Serverless, or Functions-as-a-Service (FaaS)
is a model where a software developer is only concerned about small stateless functions and the 
platform manages everything else, like deployments, ingress/egress, load-balancing, scaling...

### What are Klusterfuck functions?
Any shell script, python or nodejs program, or standalone binary can be a klusterfuck function. Functions are expected to 
read input from standard input and write to standard output. In future, I plan to support more 
runtimes.

## How does it work?
Klusterfuck works on kubernetes and kubernetes only. 
It uses a gogs (go git service) server to create git repositories for storing and versioning the code 
for functions. When creating a function with klusterfuck, a git repository is created with a default 
configuration. Developers then clone the repo and push changes to it. Klusteruck admin REST API or the 
included dashboard can be used to update or rollback to a specific commit id in the git repo.

For every function, a kubernetes deployment and service are created. A worker image 
is responsible for checking out the currently selected commit id from git and reading 
configuration and function code from it.

Functions can be connected to the outside world via flows. A flow is a directed acyclic graph (DAG) of
'connector's and functions. Connectors connect functions to HTTP endpoints (only supported method as of now),
message queues, etc. A flow DAG is asynchronously computed by propagating a vetex's output to all it's outgoing
nodes, as so on. A graphical editor is included in the dashboard.

Connectors are simple docker images which are configured using environment variables. The connector
API is always going to be very simple so that anyone can easily make a connector if one is not available
for their use case. The HTTP connector, for example, only requires one environment variable: the address 
where the request payloads will be forwarded.

### how to use:
Klusterfuck is not meant for serious use-cases yet, please only try it in a secure network using 
minikube or an isolated kubernetes deployment.

As of now, there is no security and admin and agent APIs are open to all. 

This project contains a kubernetes manifest file which will create the klusterfuck admin 
deployment and service and a gogs deployment and service. Note that both services are of ClusterIP 
type, so use port-forwarding to try them. When creating functions, it is possible to create different 
kinds of services for exposing functions over HTTP: ClusterIP and NodePort are supported. Also, when 
creating a function, it is possible to create an Ingress resource for the function.

### Roadmap
 - REST API docs
 - Dashboard tour guide
 - Integrate an API gateway
 - Throttling and metering
 - Pod horizontal scaling
 - Integrate IAM service
 - Support if/else, loops, join etc. kinds of nodes in flow

#### Installation
To deploy, for kubernetes version < 1.6, run 
 
 `kubectl create -f https://raw.githubusercontent.com/n-k/klusterfuck/master/k8s/all_k8s_15.yaml`

For kubernetes version >= 1.6, run 

`kubectl create -f https://raw.githubusercontent.com/n-k/klusterfuck/master/k8s/all.yaml`

This will create the deployment and services in 'klusterfuck' namespace.

Wait for the pods to start and reach 'running' state. Then port forward the klusterfuck-admin pod by 
running:

`kubectl -n klusterfuck port-forward $(kubectl -n klusterfuck get pod -l app=klusterfuck -o template --template="{{(index .items 0).metadata.name}}") 8080:8080`

Now you should be able to go to [http://localhost:8080](http://localhost:8080) in your favorite 
browser and reach the klusterfuck console.

#### Cleanup
Run `kubectl delete ns klusterfuck`. Note that this will also delete the gogs pod and you will lose 
all repos in gogs. Individual functions and flows can be delted via RESt APIs or the dashboard - deleting
them will also clean up associated kubernetes resources.

### Dev docs? Contribution guide?
Will add if anyone asks.

## License
Everything in this repo is distributed under the WTF public license (WTFPL). A copy of the license 
can be found in project root.
