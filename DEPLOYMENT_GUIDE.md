# Deployment Guide

Complete guide for deploying the Online School application to production.

## Prerequisites

- AWS Account (or other cloud provider)
- Docker Hub Account
- GitHub Account with repository access
- Domain name (optional)
- SSL Certificate (optional)

## Local Deployment with Docker Compose

### 1. Prepare Environment

```bash
# Clone repository
git clone https://github.com/smilar/SchoolOnline.git
cd SchoolOnline

# Create .env file
cp .env.example .env

# Edit .env with production values
nano .env
```

### 2. Build and Run

```bash
# Build images
docker-compose build

# Start services
docker-compose up -d

# Verify services
docker-compose ps

# View logs
docker-compose logs -f
```

### 3. Verify Deployment

```bash
# Check backend health
curl http://localhost:8080/actuator/health

# Check frontend
curl http://localhost:3000

# Check database
docker-compose exec db psql -U admin -d onlineschool -c "SELECT 1"
```

## AWS EC2 Deployment

### 1. Launch EC2 Instance

```bash
# SSH into instance
ssh -i your-key.pem ec2-user@your-instance-ip

# Update system
sudo yum update -y

# Install Docker
sudo yum install docker -y
sudo systemctl start docker
sudo usermod -aG docker ec2-user

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 2. Deploy Application

```bash
# Clone repository
git clone https://github.com/smilar/SchoolOnline.git
cd SchoolOnline

# Create .env file
cp .env.example .env
nano .env

# Start services
docker-compose up -d

# View logs
docker-compose logs -f
```

### 3. Configure Security Groups

In AWS Console:
- Allow port 80 (HTTP)
- Allow port 443 (HTTPS)
- Allow port 22 (SSH)

### 4. Setup SSL with Let's Encrypt

```bash
# Install Certbot
sudo yum install certbot -y

# Generate certificate
sudo certbot certonly --standalone -d your-domain.com

# Update nginx.conf with SSL configuration
# Restart services
docker-compose restart frontend
```

## Heroku Deployment

### 1. Install Heroku CLI

```bash
# macOS
brew tap heroku/brew && brew install heroku

# Linux
curl https://cli-assets.heroku.com/install.sh | sh
```

### 2. Login and Create App

```bash
# Login
heroku login

# Create app
heroku create your-app-name

# Add PostgreSQL addon
heroku addons:create heroku-postgresql:hobby-dev
```

### 3. Deploy

```bash
# Add Heroku remote
heroku git:remote -a your-app-name

# Deploy
git push heroku main

# View logs
heroku logs --tail
```

## GitHub Actions CI/CD

### 1. Configure Secrets

In GitHub repository settings, add secrets:

```
DOCKER_USERNAME: your-docker-username
DOCKER_PASSWORD: your-docker-password
DEPLOY_HOST: your-server-ip
DEPLOY_USER: deploy-user
DEPLOY_KEY: your-ssh-private-key
```

### 2. Workflows

Workflows are automatically triggered:
- On push to main/develop branches
- On pull requests
- Manual trigger with `workflow_dispatch`

### 3. Monitor Deployments

```bash
# View workflow runs
gh run list

# View specific run
gh run view <run-id>

# View logs
gh run view <run-id> --log
```

## Production Checklist

### Backend
- [ ] Database backups configured
- [ ] Environment variables set
- [ ] Security headers configured
- [ ] CORS properly configured
- [ ] Logging configured
- [ ] Monitoring enabled
- [ ] Health checks working

### Frontend
- [ ] Build optimized
- [ ] Environment variables set
- [ ] API URL correct
- [ ] Error tracking configured
- [ ] Analytics configured
- [ ] Service workers enabled

### Infrastructure
- [ ] SSL/TLS configured
- [ ] Firewall rules set
- [ ] Backups scheduled
- [ ] Monitoring alerts set
- [ ] Load balancer configured
- [ ] CDN configured (optional)

## Monitoring and Maintenance

### Health Checks

```bash
# Backend health
curl https://your-domain.com/api/actuator/health

# Frontend health
curl https://your-domain.com/

# Database health
# Check from backend logs
```

### Logs

```bash
# View backend logs
docker-compose logs backend

# View frontend logs
docker-compose logs frontend

# View database logs
docker-compose logs db

# Follow logs
docker-compose logs -f
```

### Backups

```bash
# Backup database
docker-compose exec db pg_dump -U admin onlineschool > backup.sql

# Restore database
docker-compose exec -T db psql -U admin onlineschool < backup.sql
```

### Updates

```bash
# Pull latest code
git pull origin main

# Rebuild images
docker-compose build

# Restart services
docker-compose up -d
```

## Troubleshooting

### Services Not Starting

```bash
# Check logs
docker-compose logs

# Restart services
docker-compose restart

# Rebuild and restart
docker-compose up -d --build
```

### Database Connection Issues

```bash
# Check database status
docker-compose ps db

# Check database logs
docker-compose logs db

# Verify credentials
docker-compose exec db psql -U admin -d onlineschool -c "SELECT 1"
```

### Frontend Not Loading

```bash
# Check frontend logs
docker-compose logs frontend

# Check nginx configuration
docker-compose exec frontend cat /etc/nginx/conf.d/default.conf

# Restart frontend
docker-compose restart frontend
```

### API Connection Issues

```bash
# Check backend logs
docker-compose logs backend

# Test API endpoint
curl http://localhost:8080/api/students

# Check CORS configuration
curl -H "Origin: http://localhost:3000" http://localhost:8080/api/students
```

## Performance Optimization

### Database
- Add indexes on frequently queried columns
- Enable query caching
- Optimize slow queries

### Backend
- Enable compression
- Configure connection pooling
- Use caching (Redis)

### Frontend
- Enable gzip compression
- Minify CSS/JS
- Optimize images
- Use CDN

## Security Hardening

### Network
- Use VPC
- Configure security groups
- Enable WAF
- Use VPN for admin access

### Application
- Keep dependencies updated
- Enable HTTPS
- Configure CORS properly
- Implement rate limiting
- Use strong passwords

### Data
- Enable encryption at rest
- Enable encryption in transit
- Regular backups
- Access control

## Rollback Procedure

```bash
# If deployment fails, rollback to previous version
git revert HEAD
git push origin main

# Or manually restart previous container
docker-compose down
git checkout previous-commit
docker-compose up -d
```

## Support

For deployment issues, check:
- Docker logs: `docker-compose logs`
- Application logs: Check `/var/log/` on server
- GitHub Actions: Check workflow runs
- Cloud provider console: Check resource status

---

**Last Updated:** November 26, 2025  
**Version:** 1.0.0
