variable "project_name" {
  type    = string
  default = "template-backend"
}

variable "version" {
  type = string
}

variable "jar_path" {
  type    = string
  default = "template-backend-0.1-all.jar"
}

variable "region" {
  type    = string
  default = "us-east-1"
}

source "amazon-ebs" "app" {
  ami_name      = "${var.project_name}-${var.version}"
  instance_type = "t3.small"
  region        = var.region
  source_ami_filter {
    filters = {
      name                = "al2023-ami-2023.*-x86_64"
      root-device-type    = "ebs"
      virtualization-type = "hvm"
    }
    most_recent = true
    owners      = ["amazon"]
  }
  ssh_username = "ec2-user"
  tags = {
    Name    = "${var.project_name}-${var.version}"
    Version = var.version
    Project = var.project_name
  }
}

build {
  sources = ["source.amazon-ebs.app"]

  provisioner "shell" {
    inline = [
      "sudo yum update -y",
      "sudo yum install -y java-17-amazon-corretto"
    ]
  }

  provisioner "file" {
    source      = var.jar_path
    destination = "/tmp/app.jar"
  }

  provisioner "shell" {
    inline = [
      "sudo mv /tmp/app.jar /opt/app.jar",
      "sudo chmod 644 /opt/app.jar"
    ]
  }

  provisioner "file" {
    content = <<-EOF
      [Unit]
      Description=Template Backend Service
      After=network.target

      [Service]
      Type=simple
      User=ec2-user
      WorkingDirectory=/opt
      ExecStart=/usr/bin/java -jar /opt/app.jar
      Restart=always
      RestartSec=10

      [Install]
      WantedBy=multi-user.target
    EOF
    destination = "/tmp/app.service"
  }

  provisioner "shell" {
    inline = [
      "sudo mv /tmp/app.service /etc/systemd/system/app.service",
      "sudo systemctl daemon-reload",
      "sudo systemctl enable app.service"
    ]
  }
}
