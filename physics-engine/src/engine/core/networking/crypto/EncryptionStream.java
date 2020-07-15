package engine.core.networking.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;


public class EncryptionStream 
{
	private Cipher encrypt;
	private Cipher decrypt;
	
	public EncryptionStream(Key key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{		
		decrypt = Cipher.getInstance(key.getAlgorithm());
		encrypt = Cipher.getInstance(key.getAlgorithm());
		encrypt.init(Cipher.ENCRYPT_MODE, key);
		decrypt.init(Cipher.ENCRYPT_MODE, key);
		
	}
	
	public Reader decrypt(InputStream stream) throws IOException
	{
		CipherInputStream cis = new CipherInputStream(stream, decrypt);
		return new InputStreamReader(cis);
		};
	
	public OutputStream encrypt(OutputStream stream)
	{
		CipherOutputStream cos = new CipherOutputStream(stream, decrypt);
		return cos;
	}
}
