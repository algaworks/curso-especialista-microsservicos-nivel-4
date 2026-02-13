import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  stages: [
    { duration: '5s', target: 100 },
    { duration: '5s', target: 300 },
    { duration: '5s', target: 500 },
    { duration: '10s', target: 1000 },
    { duration: '3s', target: 300 },
    { duration: '3s', target: 0 },
  ]
};

export default function() {
  let res = http.get('http://localhost:8083/api/v1/products');
  check(res, { "status is 200": (res) => res.status === 200 });
  sleep(1);
}
