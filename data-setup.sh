#!/bin/bash

echo "🚀 Populating sample data for KSQL enrichment testing..."

# 1. ACCOUNT STATUS DATA (same as before)
echo "📊 Adding account status data..."
docker exec -i agritech-kafka kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic agritech-account-status \
  --property "key.separator=:" \
  --property "parse.key=true" << EOF
AGRI-12345678:{"accountId": "AGRI-12345678", "status": "ACTIVE", "accountType": "PREMIUM", "lastUpdated": "2024-01-15T09:00:00"}
AGRI-87654321:{"accountId": "AGRI-87654321", "status": "SUSPENDED", "accountType": "BASIC", "lastUpdated": "2024-01-15T08:00:00"}
AGRI-99999999:{"accountId": "AGRI-99999999", "status": "ACTIVE", "accountType": "BASIC", "lastUpdated": "2024-01-15T07:00:00"}
AGRI-55555555:{"accountId": "AGRI-55555555", "status": "ACTIVE", "accountType": "PREMIUM", "lastUpdated": "2024-01-15T06:00:00"}
EOF

# 2. CUSTOMER RISK DATA (same as before)
echo "🎯 Adding customer risk data..."
docker exec -i agritech-kafka kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic agritech-customer-risk \
  --property "key.separator=:" \
  --property "parse.key=true" << EOF
AGRI-12345678:{"accountId": "AGRI-12345678", "riskLevel": "MEDIUM", "lastAssessment": "2024-01-10T14:00:00"}
AGRI-99999999:{"accountId": "AGRI-99999999", "riskLevel": "HIGH", "lastAssessment": "2024-01-12T10:00:00"}
AGRI-55555555:{"accountId": "AGRI-55555555", "riskLevel": "LOW", "lastAssessment": "2024-01-13T11:00:00"}
EOF

# 3. TRANSACTION LIMITS DATA (same as before)
echo "💰 Adding transaction limits data..."
docker exec -i agritech-kafka kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic agritech-transaction-limits \
  --property "key.separator=:" \
  --property "parse.key=true" << EOF
PREMIUM:{"accountType": "PREMIUM", "dailyLimit": 50000.00, "singleTxnLimit": 10000.00}
BASIC:{"accountType": "BASIC", "dailyLimit": 10000.00, "singleTxnLimit": 2000.00}
EOF

# 4. REAL WITHDRAWAL TRANSACTIONS
echo "💳 Adding real withdrawal transactions..."
docker exec -i agritech-kafka kafka-console-producer \
  --bootstrap-server localhost:9092 \
  --topic agritech-transactions \
  --property "key.separator=:" \
  --property "parse.key=true" << EOF
AGRI-12345678:{"transactionType": "WITHDRAWAL", "transactionId": "TXN-001", "accountId": "AGRI-12345678", "amount": 15000.00, "currency": "USD", "timestamp": "2024-01-15T10:30:00", "location": "NYC ATM", "channel": "ATM", "status": "PENDING", "cardNumber": "1234567890123456", "withdrawalMode": "ATM", "atmLocation": "Times Square ATM"}
AGRI-87654321:{"transactionType": "WITHDRAWAL", "transactionId": "TXN-002", "accountId": "AGRI-87654321", "amount": 500.00, "currency": "USD", "timestamp": "2024-01-15T10:31:00", "location": "LA Branch", "channel": "BRANCH", "status": "PENDING", "cardNumber": "9876543210987654", "withdrawalMode": "BRANCH", "atmLocation": null}
AGRI-99999999:{"transactionType": "WITHDRAWAL", "transactionId": "TXN-003", "accountId": "AGRI-99999999", "amount": 5000.00, "currency": "USD", "timestamp": "2024-01-15T10:32:00", "location": "Chicago ATM", "channel": "ATM", "status": "PENDING", "cardNumber": "1111222233334444", "withdrawalMode": "ATM", "atmLocation": "Downtown ATM"}
AGRI-55555555:{"transactionType": "WITHDRAWAL", "transactionId": "TXN-004", "accountId": "AGRI-55555555", "amount": 500.00, "currency": "USD", "timestamp": "2024-01-15T10:33:00", "location": "Online", "channel": "ONLINE", "status": "PENDING", "cardNumber": "5555666677778888", "withdrawalMode": "ONLINE", "atmLocation": null}
EOF

echo "✅ Sample data population complete!"