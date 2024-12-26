
INSERT INTO authority VALUES (1, 'role_oauth_admin');
INSERT INTO authority VALUES (2, 'role_admin');
INSERT INTO authority VALUES (3, 'role_user');
INSERT INTO credentials VALUES (1, 1, 'oauth_admin', 'admin', 0);
INSERT INTO credentials VALUES (2, 1, 'resource_admin', 'admin', 0);
INSERT INTO credentials VALUES (3, 1, 'user', 'user', 0);
INSERT INTO credentials VALUES (4, 1, 'config', 'password', 0);
INSERT INTO credentials VALUES (5, 0, 'disable', 'password', 0);
INSERT INTO credentials_authorities VALUES (1, 1);
INSERT INTO credentials_authorities VALUES (2, 2);
INSERT INTO credentials_authorities VALUES (3, 2);
INSERT INTO credentials_authorities VALUES (4, 1);
INSERT INTO credentials_authorities VALUES (5, 1);

INSERT INTO oauth_client_details
    (client_id, resource_ids, client_secret, 
    scope,
    authorized_grant_types, web_server_redirect_uri,
    authorities, 
    access_token_validity, refresh_token_validity, 
    additional_information, autoapprove)
VALUES
  ('user-client', 'user-service', 'clientSecret', 
  'read,write,password,authorization_code,refresh_token',
  'client_credentials', 'http://user-service.k8s.localtest.me:6001/',
  'role_admin', 
  3600, 3600, 
  null, 'true');
  
  
INSERT INTO oauth_client_details
(client_id, resource_ids, client_secret,
scope, authorized_grant_types, web_server_redirect_uri, 
authorities, 
access_token_validity, refresh_token_validity, 
additional_information, autoapprove)
VALUES
  ('read-only-client', 'todo-services', NULL, 
  'read', 'implicit', 'http://localhost', NULL, 
  7200, 0, 
  NULL, 'false');

INSERT INTO oauth_client_details
(client_id, resource_ids, client_secret, 
scope, 
authorized_grant_types, web_server_redirect_uri, 
authorities, 
access_token_validity, refresh_token_validity, 
additional_information, autoapprove)
VALUES
  ('curl-client', 'todo-services', 'client-secret', 
  'read,write', 
  'client_credentials', '', 
  'role_admin', 
  7200, 0, 
  NULL, 'false');

INSERT INTO oauth_client_details
(client_id, client_secret)
VALUES
  ('resource-server', 'resource-server');