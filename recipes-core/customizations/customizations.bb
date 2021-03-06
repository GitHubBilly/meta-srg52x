#
# SRG-335x Debian Buster
#
# Copyright (c) LYD/AAEON, 2020
#
# Authors:
#  KunYi <kunyi.chen@gmail.com>
#
# SPDX-License-Identifier: MIT
#

inherit dpkg-raw

DESCRIPTION = "SRG-3352C Debian Buster image"

SRC_URI = " \
	file://postinst			\
	file://99-silent-printk.conf	\
	file://usbgadget.conf		\
	file://usbgadget.opt		\
	"

DEPENDS += "sshd-regen-keys"

DEBIAN_DEPENDS = " \
	ifupdown, isc-dhcp-client, net-tools, iputils-ping, ssh, sshd-regen-keys"

do_install() {
	install -v -d ${D}/etc/modules-load.d
	install -v -m 644 ${WORKDIR}/usbgadget.conf	${D}/etc/modules-load.d
	install -v -d ${D}/etc/modprobe.d
	install -v -m 644 ${WORKDIR}/usbgadget.opt	${D}/etc/modprobe.d/usbgadget.conf

	install -v -d ${D}/etc/sysctl.d
	install -v -m 644 ${WORKDIR}/99-silent-printk.conf ${D}/etc/sysctl.d/

	# install -v -d ${D}/etc/srg52
	# install -v -m 644 ${WORKDIR}/srg52.img		${D}/etc/srg52/

	install -v -d ${D}/etc/systemd/system/getty.target.wants
	( cd ${D}/etc/systemd/system/getty.target.wants && ln -s /lib/systemd/system/serial-getty@.service serial-getty@ttyGS0.service )

	# add hooks for initramfs
	# HOOKS=${D}/etc/initramfs-tools/hooks
	# install -m 0755 -d ${HOOKS}
	# install -m 0740 ${WORKDIR}/usbgadget.hook	${HOOKS}/usbgadget.hook
}

addtask do_install after do_transform_template
