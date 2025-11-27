# SPDX-FileCopyrightText: 2025 SUSE LLC
#
# SPDX-License-Identifier: Apache-2.0
FROM registry.opensuse.org/opensuse/tumbleweed:latest

# Installing all packages
RUN zypper refresh && \
        zypper -n install awk bash-completion curl expect git man openssh sudo tar vim wget \
                          nodejs22 java-25-openjdk-devel java-17-openjdk-devel maven && \
         zypper clean -a

# Setting Java 17 as system default
RUN /usr/sbin/alternatives --set javac /usr/lib64/jvm/java-17-openjdk/bin/javac && \
        /usr/sbin/alternatives --set java /usr/lib64/jvm/jre-17-openjdk/bin/java && \
        /usr/sbin/alternatives --set jre_openjdk /usr/lib64/jvm/jre-17-openjdk && \
        /usr/sbin/alternatives --set java_sdk_openjdk /usr/lib64/jvm/java-17-openjdk

# Creating regular user
RUN useradd --create-home --shell /bin/bash matcher
RUN echo "matcher ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

USER matcher
