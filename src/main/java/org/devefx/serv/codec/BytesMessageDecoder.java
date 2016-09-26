package org.devefx.serv.codec;

import io.netty.buffer.ByteBuf;

import org.devefx.serv.core.MessageDecoder;
import org.devefx.serv.core.MessageEvent;
import org.devefx.serv.net.Sender;

public class BytesMessageDecoder implements MessageDecoder<byte[]> {
	@Override
	public MessageEvent<byte[]> decode(ByteBuf buf, Sender sender) {
		short id = buf.readShort();
		byte[] bytes;
		if (buf.hasArray()) {
			bytes = buf.array();
		} else {
			bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
		}
		return new MessageEvent<byte[]>(id, bytes, sender);
	}
}
