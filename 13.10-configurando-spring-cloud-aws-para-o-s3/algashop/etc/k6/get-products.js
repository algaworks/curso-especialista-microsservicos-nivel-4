import http from 'k6/http';
import { sleep, check } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8083';

export const options = {
  scenarios: {
      get_products_smoke_test: {
        executor: 'constant-vus',
        vus: 1,
        duration: '5s'
      },
      get_products_load_test: {
        executor: 'constant-arrival-rate',
        rate: 100,
        timeUnit: '1s',
        duration: '1m',
        startTime: '5s',
        maxVUs: 200,
        preAllocatedVUs: 50
      }
  },
  thresholds: {
    http_req_duration: ['p(95) < 800'],
    http_req_failed: ['rate < 0.01']
  }
};

export default function() {
  const url = `${BASE_URL}/api/v1/products`
  let res = http.get(url);
  check(res, { "status is 200": (res) => res.status === 200 });
  sleep(1);
}
