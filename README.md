# bookmark-service

## kubectl commands

### Get a list of all namespaces in the cluster
`kubectl get namespace`

### Get deployments in the ns-bookmark-service namespace
`kubectl get deployment -n ns-bookmark-service`

### get a list of all pods in the namespace ns-bookmark-service
`kubectl get pods -n ns-bookmark-service`

### Apply portforwarding from local to postgres so that we can provide support and access the database
`kubectl port-forward pod/postgres-5bb96bdb5-j4bwg 9432:5432 -n ns-bookmark-service`

or

`kubectl port-forward deployment/postgres 9432:5432 -n ns-bookmark-service`




## sql 

```sql
-- delete bookmarks
delete from bookmarks where group_id in (
	select id from "group" where user_id in (
		select user_id from "user" where username = 'bob'
	)
)

-- delete groups
delete from "group" where user_id in (
	select user_id from "user" where username = 'bob'
)


-- delete user
delete from "user" where username = 'bob'
```