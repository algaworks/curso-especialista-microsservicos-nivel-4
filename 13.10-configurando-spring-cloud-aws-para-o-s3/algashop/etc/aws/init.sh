#!/bin/bash

AWS_ACCESS_KEY_ID=LS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY=LS_SECRET_ACCESS_KEY
AWS_DEFAULT_REGION=LS_REGION

awslocal s3 mb s3://algashop-product-image

awslocal s3api put-bucket-cors --bucket algashop-product-image --cors-configuration file:///etc/aws/cors.json

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 1a5ac2f0-1100-4368-9ea1-1f62ebe25468.png \
  --content-type image/png \
  --body /etc/images/1a5ac2f0-1100-4368-9ea1-1f62ebe25468.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 1dc7a3ae-28e5-4260-96aa-060d865f79ad.png \
  --content-type image/png \
  --body /etc/images/1dc7a3ae-28e5-4260-96aa-060d865f79ad.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 6e920c7c-e8af-4b28-a4db-9ca05614194a.jpg \
  --content-type image/jpeg \
  --body /etc/images/6e920c7c-e8af-4b28-a4db-9ca05614194a.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 7f66b19e-edcc-4e16-a1d4-6c17081c86db.png \
  --content-type image/png \
  --body /etc/images/7f66b19e-edcc-4e16-a1d4-6c17081c86db.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 17d8be5b-f046-423e-bc55-9d2fae438e17.png \
  --content-type image/png \
  --body /etc/images/17d8be5b-f046-423e-bc55-9d2fae438e17.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 513e2b9c-85f9-4245-8807-b2a54e3bdce1.jpg \
  --content-type image/jpeg \
  --body /etc/images/513e2b9c-85f9-4245-8807-b2a54e3bdce1.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 0914a469-06e7-4275-afca-f9c71e8e056c.jpg \
  --content-type image/jpeg \
  --body /etc/images/0914a469-06e7-4275-afca-f9c71e8e056c.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 48746f11-23ea-48dd-923a-d9b8ca07279a.png \
  --content-type image/png \
  --body /etc/images/48746f11-23ea-48dd-923a-d9b8ca07279a.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key 67237b0c-0823-4cb1-a7fb-0ba1bfb46331.png \
  --content-type image/png \
  --body /etc/images/67237b0c-0823-4cb1-a7fb-0ba1bfb46331.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key a71a1121-1e75-4ed1-a3f2-fabe29ab95bd.jpg \
  --content-type image/jpeg \
  --body /etc/images/a71a1121-1e75-4ed1-a3f2-fabe29ab95bd.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key ac8ded45-26c1-41f3-9ec4-0ff8c58366d7.jpg \
  --content-type image/jpeg \
  --body /etc/images/ac8ded45-26c1-41f3-9ec4-0ff8c58366d7.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key ad96fad7-921f-4f0b-b4ac-020b91fb4b61.jpg \
  --content-type image/jpeg \
  --body /etc/images/ad96fad7-921f-4f0b-b4ac-020b91fb4b61.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key b4729d25-65bc-450d-b860-3781ad86e6b8.png \
  --content-type image/png \
  --body /etc/images/b4729d25-65bc-450d-b860-3781ad86e6b8.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key babb525b-776e-45a5-a3d6-7e63330306b1.jpg \
  --content-type image/jpeg \
  --body /etc/images/babb525b-776e-45a5-a3d6-7e63330306b1.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key bc8682c3-4d95-4fd4-b965-9d193a160860.png \
  --content-type image/png \
  --body /etc/images/bc8682c3-4d95-4fd4-b965-9d193a160860.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key d519e52c-fd35-48b9-85e9-c3e1deaa32cf.jpg \
  --content-type image/jpeg \
  --body /etc/images/d519e52c-fd35-48b9-85e9-c3e1deaa32cf.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key d696d0fe-0ce5-497c-bcc5-0e2e679e3b11.jpg \
  --content-type image/jpeg \
  --body /etc/images/d696d0fe-0ce5-497c-bcc5-0e2e679e3b11.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key d9061b77-5336-4fb5-9397-a3cb92f5f785.jpg \
  --content-type image/jpeg \
  --body /etc/images/d9061b77-5336-4fb5-9397-a3cb92f5f785.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key e12638c1-554e-4489-b9a4-2ad890e3d63f.jpg \
  --content-type image/jpeg \
  --body /etc/images/e12638c1-554e-4489-b9a4-2ad890e3d63f.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key f664b402-05d0-42c3-8a6b-7f6a5214c650.png \
  --content-type image/png \
  --body /etc/images/f664b402-05d0-42c3-8a6b-7f6a5214c650.png &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key fd97856c-29c4-4349-8546-46d159f1f621.jpg \
  --content-type image/jpeg \
  --body /etc/images/fd97856c-29c4-4349-8546-46d159f1f621.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key fe0b141d-96fc-4c63-b1f5-fc535e2874e8.jpg \
  --content-type image/jpeg \
  --body /etc/images/fe0b141d-96fc-4c63-b1f5-fc535e2874e8.jpg &

awslocal s3api put-object \
  --bucket algashop-product-image \
  --key fe7787a7-8f86-4a1b-8fd8-8add0b51aa35.jpg \
  --content-type image/.jpg \
  --body /etc/images/fe7787a7-8f86-4a1b-8fd8-8add0b51aa35.jpg &
