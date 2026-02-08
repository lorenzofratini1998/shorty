# Shorty

<p align="left"> 
<img src="https://img.shields.io/badge/Java_21-ED8B00?logo=openjdk&logoColor=white" /> 
<img src="https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=flat-square&logo=Spring&logoColor=white" /> 
<img src="https://img.shields.io/badge/AWS_Lambda-FF9900?logo=amazonaws&logoColor=white" /> 
<img src="https://img.shields.io/badge/DynamoDB-4053D6?logo=amazondynamodb&logoColor=white" /> 
<img src="https://img.shields.io/badge/AWS_SAM-CA4245?logo=amazonaws&logoColor=white" /> 

<img src="https://img.shields.io/badge/GitHub_Actions-2088FF?logo=githubactions&logoColor=white" /> 
<img src="https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white" /> 
<img src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white" />

<img src="https://img.shields.io/badge/Angular_21+-DD0031?logo=angular&logoColor=white" /> 
</p>

Shorty is a **cloud-native, serverless URL shortener** engineered to demonstrate high-performance Backend architecture on AWS.

While it includes a modern UI for demonstration purposes, the core project focuses on **optimizing Java 21 for Serverless environments**, implementing **Infrastructure as Code (IaC)**, and designing scalable **NoSQL data models**.

🔗 **Live Demo:** [https://lorenzofratini1998.github.io/shorty/](https://lorenzofratini1998.github.io/shorty/)

## 🚀 Backend Architecture

The system is built with a **Serverless-First** approach to ensure scalability, zero-maintenance, and cost-efficiency.

<p align="center">
  <img 
    width="600"
    src="https://github.com/user-attachments/assets/327f7234-c5e6-424d-9442-688d04980903"
    alt="architecture"
  />
</p>

### ☁️ Core Technologies
* **Runtime:** **Java 21** with **Spring Boot 3.5**.
* **Compute:** **AWS Lambda** deployed via **AWS SAM** (Serverless Application Model).
* **Database:** **Amazon DynamoDB**, chosen for its single-digit millisecond latency and seamless scaling for key-value lookups.
* **API Gateway:** AWS Lambda Function URLs for low-latency HTTP invocation.

### ⚡ Performance & Optimization
A key challenge addressed in this project is the **Java Cold Start** issue in serverless environments.
* **Tiered Compilation:** Configured to balance startup time vs. peak performance.
* **Lightweight Container:** Minimized dependencies to reduce artifact size and init duration.
* **DynamoDB Optimization:** Utilized efficient `GetItem` patterns to ensure constant-time retrieval regardless of dataset size.

## 🛠 DevOps & CI/CD

The project utilizes a **Monorepo** structure with decoupled pipelines managed by **GitHub Actions**.

* **Infrastructure as Code:** The entire AWS stack (Lambda, Tables) is defined in a `template.yml` file, enabling reproducible deployments across environments (Dev/Prod).
* **Automated Backend Pipeline:**
    1.  **Build & Test:** Maven Verify execution.
    2.  **Static Analysis:** SonarCloud integration for code quality gates.
    3.  **Deploy:** Automatic provisioning to AWS via SAM CLI upon merge to `master`.

## 💻 Frontend Overview

A lightweight **Angular 21+** Single Page Application (SPA) serves as the client interface.
It features **Signal-based reactivity**, real-time input validation, and LocalStorage persistence for user history, styled with **TailwindCSS** for a responsive experience.

## 🔧 Getting Started
To run this project, you need an AWS account. The infrastructure (DynamoDB Table, Lambda Functions) is defined as code (IaC) and needs to be provisioned first.

### Prerequisites
* [**Java JDK 21**](https://adoptium.net/)
* [**AWS CLI**](https://aws.amazon.com/cli/) (Configured with `aws configure`)
* [**AWS SAM CLI**](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)
* **Maven**

#### Provision Infrastructure

First, deploy the stack to your own AWS account to create the DynamoDB table and Lambda function.
```bash
cd shorty-be

# Build the project using SAM
sam build

# Deploy the stack (Interactive mode)
sam deploy --guided
```

*Follow the prompts (you can accept defaults) When asked "Stack Name", use something like `shorty-stack-dev`*. Once finished, SAM will output the API Gateway Endpoint and the created resources.

#### Run Locally
You can run the Spring Boot application on your machine (localhost:8080) while connecting to the real DynamoDB table created in step 1.

You need to override the TABLE_NAME environment variable, as the cloud table name will be unique (e.g., `shorty-stack-dev-UrlShortenerTable-XXXX`).

**Mac/Linux**
```bash
# Replace 'YOUR_TABLE_NAME' with the physical ID from AWS Console or CloudFormation outputs
TABLE_NAME=shorty-stack-dev-UrlShortenerTable-XXXX mvn spring-boot:run
```

**Windows (PowerShell)**
```bash
$env:TABLE_NAME="shorty-stack-dev-UrlShortenerTable-XXXX"
mvn spring-boot:run
```

#### Frontend Setup
Once the backend is ready, you can start the Angular UI.
```bash
cd shorty-fe
npm install
npm start
```
*Note: You might need to update src/environments/environment.ts with your new local or cloud API URL.*

## 📘 What I Learned
This project was a deep dive into modern Cloud Engineering:

* **Serverless Java Pattern**: Adapting Spring Boot for ephemeral environments (Lambda) vs. traditional containers to optimize cold starts and resource usage.
* **Infrastructure as Code**: Mastering **AWS SAM** to define serverless resources (Functions, Tables, Policies) declaratively.
* **NoSQL Design**: Modeling **DynamoDB** access patterns (Partition Keys) to ensure consistent performance under high-read throughput scenarios.

## 📄 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

## ✍️ Authors

- [@Lorenzo Fratini](https://www.github.com/lorenzofratini1998)
