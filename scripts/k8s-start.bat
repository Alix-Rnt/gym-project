kubectl create configmap nginx-config --from-file=nginx.conf=./gateway/nginx.conf
kubectl create configmap prometheus-config --from-file=prometheus.yml=./monitoring/prometheus.yml
kubectl apply -f k8s/