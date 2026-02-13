import http from 'k6/http';
import { sleep, check } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
  scenarios: {
      buy_now_smoke_test: {
        executor: 'constant-vus',
        vus: 1,
        duration: '5s'
      },
      buy_now_volume_test: {
        executor: 'constant-vus',
        vus: 50,
        duration: '5s'
      }
  },
  thresholds: {
    http_req_duration: ['p(95) < 1200']
  }
};

export default function() {
  const url = `${BASE_URL}/api/v1/orders`;
  const params = {
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/vnd.order-with-product.v1+json'
    }
  }
  const payload = {
    paymentMethod: 'GATEWAY_BALANCE',
    shipping: {
      recipient: {
        firstName: 'John',
        lastName: 'Doe',
        document: '255-08-0578',
        phone: '478-256-2604'
      },
      address: {
        street: 'Elm Street',
        number: '456',
        complement: 'House A',
        neighborhood: 'Central Park',
        city: 'Springfield',
        state: 'Illinois',
        zipCode: '62704'
      }
    },
    billing: {
      firstName: 'Matt',
      lastName: 'Damon',
      phone: '123-321-1112',
      document: '123-45-6789',
      email: 'matt.damon@email.com',
      address: {
        street: 'Amphitheatre Parkway',
        number: '1600',
        complement: '',
        neighborhood: 'Mountain View',
        city: 'Mountain View',
        state: 'California',
        zipCode: '94043'
      }
    },
    productId: 'a5638e2f-1b9a-4265-88fb-634dd5f5fe5d',
    customerId: '6e148bd5-47f6-4022-b9da-07cfaa294f7a',
    quantity: 2
  };

  const json = JSON.stringify(payload);
  let res = http.post(url, json, params);
  check(res, { "status is 201": (res) => res.status === 201 });
  sleep(1);
}
