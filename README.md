# 🏦 AgriTech Bank Kafka Streaming Platform

A comprehensive Apache Kafka streaming platform designed for banking transaction processing, enterprise-grade event-driven architecture patterns.

## 🎯 **Project Overview**

This project showcases  Kafka streaming capabilities for financial services, implementing real-time transaction processing with fraud detection, audit compliance, and exactly-once semantics required for banking applications.

### **Key Features**
- **Banking-Grade Reliability**: Exactly-once delivery, idempotent producers, proper error handling
- **Polymorphic Transaction Models**: Support for deposits, withdrawals, bill payments, and salary credits
- **Enterprise Architecture**: Microservices with event-driven communication
- **Real-time Processing**: Stream processing for fraud detection and compliance
- **Production-Ready**: Comprehensive monitoring, logging, and error handling

## 🏗️ **Architecture**

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────────┐
│   REST Client   │───▶│ Transaction API  │───▶│   Kafka Topics      │
└─────────────────┘    │    Service       │    │                     │
                       └──────────────────┘    │ agritech-          │
                                               │ transactions        │
                                               └─────────┬───────────┘
                                                         │
                                               ┌─────────▼───────────┐
                                               │ Transaction         │
                                               │ Processor Service   │
                                               │ (Kafka Streams)     │
                                               └─────────┬───────────┘
                                                         │
                                     ┌───────────────────┼───────────────────┐
                                     ▼                   ▼                   ▼
                           ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
                           │ Fraud Alerts    │ │ Audit Trail     │ │ Account         │
                           │ Topic           │ │ Topic           │ │ Balances        │
                           └─────────────────┘ └─────────────────┘ └─────────────────┘
```

## 📊 **Technology Stack**

| Component | Technology | Version |
|-----------|------------|---------|
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 3.2.0 |
| **Messaging** | Apache Kafka | Latest |
| **Stream Processing** | Kafka Streams | Latest |
| **Build Tool** | Maven | 3.x |
| **Serialization** | Jackson JSON | 2.15.x |
| **Infrastructure** | Docker Compose | Latest |

## 🚀 **Quick Start**

### **Prerequisites**
- Java 17+
- Maven 3.6+
- Docker & Docker Compose

### **1. Clone Repository**
```bash
git clone https://github.com/YOUR_USERNAME/agritech-kafka-streaming-platform.git
cd agritech-kafka-streaming-platform
```

### **2. Start Infrastructure**
```bash
# Start Kafka and ZooKeeper
docker-compose up -d

# Verify Kafka is running
docker ps | grep kafka
```

### **3. Start Transaction API Service**
```bash
# Build all modules
mvn clean compile

# Start the API service
mvn spring-boot:run -pl transaction-api-service
```

### **4. Test Transaction Processing**
```bash
# Create a deposit transaction
curl -X POST http://localhost:8080/api/v1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "transactionType": "DEPOSIT",
    "transactionId": "TXN-DEMO-001",
    "depositMode": "ATM",
    "channel": "MOBILE",
    "accountId": "AGRI-12345678",
    "amount": 1000.50,
    "currency": "USD",
    "description": "Demo deposit transaction"
  }'

# Monitor Kafka messages
docker exec -it kafka kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic agritech-transactions \
  --from-beginning
```

## 💼 **Transaction Types Supported**

### **1. Deposit Transactions**
```json
{
  "transactionType": "DEPOSIT",
  "depositMode": "ATM|CHECK|ELECTRONIC",
  "checkNumber": "optional",
  "depositorAccountId": "optional"
}
```

### **2. Withdrawal Transactions**
```json
{
  "transactionType": "WITHDRAWAL",
  "withdrawalMode": "ATM|BRANCH",
  "cardNumber": "optional",
  "atmLocation": "optional"
}
```

### **3. Bill Payment Transactions**
```json
{
  "transactionType": "BILL_PAYMENT",
  "billId": "required",
  "payeeId": "required"
}
```

### **4. Salary Transactions**
```json
{
  "transactionType": "SALARY",
  "payPeriod": "required",
  "employerId": "required",
  "payrollReference": "optional"
}
```

## 🏛️ **Banking-Grade Features**

### **Producer Reliability**
- **Exactly-once delivery**: `enable.idempotence=true`
- **All replica acknowledgment**: `acks=all`
- **Infinite retries with timeout**: `retries=2147483647`
- **Ordering guarantees**: `max.in.flight.requests.per.connection=1`

### **Performance Optimization**
- **Batch processing**: `batch.size=65536`
- **Compression**: `compression.type=snappy`
- **Memory management**: `buffer.memory=33554432`
- **Latency optimization**: `linger.ms=10`

### **Monitoring & Observability**
- **Health checks**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Application info**: `/actuator/info`
- **Comprehensive logging**: Structured logging with correlation IDs

## 📈 **Kafka Topics Configuration**

| Topic | Partitions | Use Case |
|-------|------------|----------|
| `agritech-transactions` | 4 | Primary transaction stream |
| `agritech-fraud-alerts` | 3 | Suspicious transaction alerts |
| `agritech-audit-trail` | 2 | Regulatory compliance logging |

**Partitioning Strategy**: Account ID based for transaction ordering guarantees per account.

## 🛠️ **Development Status**

### ✅ **Completed (Phase 1)**
- [x] Shared transaction models with polymorphic design
- [x] REST API service with Kafka producer
- [x] Banking-grade producer configuration
- [x] Docker Compose infrastructure
- [x] All transaction types tested and working
- [x] Proper JSON serialization with validation
- [x] Comprehensive error handling

### 🚧 **In Progress (Phase 2)**
- [ ] Transaction processor service (Kafka Streams)
- [ ] Real-time fraud detection algorithms
- [ ] Windowed aggregations for velocity checks
- [ ] Account balance state management
- [ ] Dead letter queue handling

### 📋 **Planned (Phase 3)**
- [ ] Interactive queries for real-time account balances
- [ ] Advanced stream processing patterns
- [ ] Performance benchmarking and optimization
- [ ] Monitoring dashboards
- [ ] Schema registry integration

## 🧪 **Testing**

### **API Endpoints**
- **POST** `/api/v1/transactions` - Submit new transaction
- **GET** `/actuator/health` - Service health check
- **GET** `/actuator/metrics` - Application metrics

### **Sample Test Data**
See `docs/sample-transactions.json` for comprehensive test scenarios.

## 🔧 **Configuration**

### **Application Properties**
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      acks: all
      enable-idempotence: true
      retries: 2147483647

agritech:
  banking:
    max-transaction-amount: 50000.00
    daily-transaction-limit: 100000.00
```

## 📚 **Documentation**

- **API Documentation**: See `/docs/api.md`
- **Architecture Decision Records**: See `/docs/adr/`
- **Deployment Guide**: See `/docs/deployment.md`

## 🤝 **Contributing**

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🏆 **Use Cases Demonstrated**

This platform demonstrates advanced Kafka patterns suitable for:
- **Investment Banking**: Trade settlement and clearing
- **Retail Banking**: Customer transaction processing
- **Payment Processing**: Real-time payment validation
- **Risk Management**: Fraud detection and compliance
- **Regulatory Reporting**: Audit trail and compliance logging

---

**Built with ❤️ for enterprise-grade financial streaming applications**
